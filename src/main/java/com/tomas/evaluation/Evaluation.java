package com.tomas.evaluation;

import com.tomas.model.InputResponse;
import com.tomas.util.Util;
import com.tomas.validator.DafnyToAlloyConverter;
import com.tomas.validator.DafnyTranslator;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Evaluation {

    DafnyTranslator dafnyTranslator = new DafnyTranslator();
    DafnyToAlloyConverter dafnyToAlloyConverter = new DafnyToAlloyConverter(dafnyTranslator);
    private int totalCounter = 0;
    private int counterCorrectCode = 0;
    private int counterCorrectSpecs = 0;
    private int counterConsistencyDetections = 0;
    private boolean correctCode;
    private boolean correctSpecs;

    public void evaluateIndividual(CodeTask codeTask) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/input";

        String message = codeTask.getText() + "(integer problem)";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<InputResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, InputResponse.class);

        String codeCleaned = response.getBody().getCodeGenerated().replaceAll("(?s)def main\\(\\):.*", "");
        String specs = response.getBody().getSpecsGenerated();

        if (evaluateSpecs(specs, codeTask.getTask_id())) {
            System.out.println("Specs evaluation passed! Problem nr" + (totalCounter+1));
            correctSpecs = true;
            counterCorrectSpecs++;
        } else {
            correctSpecs = false;
            System.err.println("Specs evaluation failed!");
        }

        if (evaluateCode(codeCleaned, codeTask.getTest_list())) {
            System.out.println("Code evaluation passed!");
            correctCode = true;
            counterCorrectCode++;
        } else {
            correctCode = false;
            System.err.println("Code evaluation failed!");
        }
        if (!correctCode && !correctSpecs) {
            System.out.println("Code and specs evaluation failed!");
        } else if (correctSpecs && correctCode && response.getBody().getResult() == 1.0) {
            System.out.println("Code and specs evaluation passed!");
            counterConsistencyDetections++;
            totalCounter++;
        } else if (correctSpecs && !correctCode && response.getBody().getResult() < 1.0) {
            System.out.println("Code evaluation failed but specs evaluation passed!");
            counterConsistencyDetections++;
            totalCounter++;
        } else if (!correctSpecs && correctCode && response.getBody().getResult() < 1.0) {
            System.out.println("Code evaluation passed but specs evaluation failed!");
            counterConsistencyDetections++;
            totalCounter++;
        } else {
            System.out.println("Consistency wrongly detected!");
            totalCounter++;
        }
    }

    public boolean evaluateCode(String codeCleaned, List<String> codeTaskTestList) throws IOException, InterruptedException {
        String methodName = getMethodName(codeCleaned);

        File tempFile = new File("src/main/java/com/tomas/evaluation/tempFile.py");
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(codeCleaned + "\n");
        for (String assertion : codeTaskTestList) {
            assertion = assertion.replaceAll("(?<=assert\\s)(\\w+)(?=\\()", methodName);
            fileWriter.write(assertion + "\n");
        }
        fileWriter.close();

        ProcessBuilder processBuilder = new ProcessBuilder("python", tempFile.getPath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        Files.delete(tempFile.toPath());

        if (exitCode == 0) {
            System.out.println("All assertions passed!");
            return true;
        } else {
            System.err.println("Assertion failed:\n" + output);
            return false;
        }
    }

    public boolean evaluateSpecs(String specs, int task_id) throws IOException, InterruptedException {
        Map<String, String> specsConditions = dafnyTranslator.extractSpecs(specs);
        String methodSignature = dafnyTranslator.extractMethodSignature(specs);
        List<String> params = new ArrayList<>();
        List<String> returns = new ArrayList<>();
        String variables = "";

        Matcher matcher = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignature);
        if (matcher.find()) {
            String paramsRaw = matcher.group(1);
            String returnsRaw = matcher.group(2);

            params = Util.extractVariableNames(paramsRaw);
            returns = Util.extractVariableNames(returnsRaw);
            variables = paramsRaw + ", " + returnsRaw;
        }

        String taskIdFile = "task_id_" + task_id + ".dfy";
        String codeInFile = Files.readString(Path.of("src/main/java/com/tomas/evaluation/dafny/" + taskIdFile));

        Map<String, String> specsConditionsFile = dafnyTranslator.extractSpecs(codeInFile);
        String methodSignatureFile = dafnyTranslator.extractMethodSignature(codeInFile);

        Matcher matcherFile = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignatureFile);
        if (matcherFile.find()) {
            String paramsRaw = matcherFile.group(1);
            String returnsRaw = matcherFile.group(2);

            List<String> paramsFile = Util.extractVariableNames(paramsRaw);
            List<String> returnsFile = Util.extractVariableNames(returnsRaw);

            for (int i = 0; i < paramsFile.size(); i++) {
                if (specsConditionsFile.get("precondition") == null) {
                    specsConditionsFile.put("precondition", "");
                    break;
                }
                String newPrecondition = specsConditionsFile.get("precondition").replaceAll(paramsFile.get(i), params.get(i));
                specsConditionsFile.put("precondition", newPrecondition);
                String newPreconditionOriginal = specsConditions.get("precondition").replaceAll(";", "");
                specsConditions.put("precondition", newPreconditionOriginal);
                String newPostcondition = specsConditionsFile.get("postcondition").replaceAll(paramsFile.get(i), params.get(i));
                specsConditionsFile.put("postcondition", newPostcondition);
            }
            for (int i = 0; i < returnsFile.size(); i++) {
                if (specsConditionsFile.get("postcondition") == null) {
                    specsConditionsFile.put("postcondition", "");
                    break;
                }
                String newPostcondition = specsConditionsFile.get("postcondition").replaceAll(returnsFile.get(i), returns.get(i));
                newPostcondition = newPostcondition.replaceAll( ";", "");
                specsConditionsFile.put("postcondition", newPostcondition);
                String newPostconditionOriginal = specsConditions.get("postcondition").replaceAll(";", "");
                specsConditions.put("postcondition", newPostconditionOriginal);
            }
        }

        return equivalentConditions(variables, specsConditions.get("precondition"), specsConditionsFile.get("precondition")) && equivalentConditions(variables, specsConditions.get("postcondition"), specsConditionsFile.get("postcondition"));
    }

    public boolean equivalentConditions(String variables, String conditionGenerated, String conditionFile) throws IOException, InterruptedException {
        String code = generate(variables, conditionGenerated, conditionFile);
        Path dafnyFile = write(code);

        return verify(dafnyFile);
    }

    public String getMethodName(String code) {
        Pattern pattern = Pattern.compile("(?<=def\\s)(\\w+)(?=\\()");
        Matcher matcher = pattern.matcher(code);

        String methodName = "";
        if (matcher.find()) {
            methodName = matcher.group(1);
        }

        return methodName;
    }

    public String generate(String variables, String cond1, String cond2) {
        String paramNames = extractParamNames(variables);
        return """
            lemma CheckEquivalence()
              ensures forall %s :: Cond1(%s) <==> Cond2(%s)
            {
              forall %s
                ensures Cond1(%s) <==> Cond2(%s)
              {
                if Cond1(%s) {
                  assert Cond2(%s);
                }
                if Cond2(%s) {
                  assert Cond1(%s);
                }
              }
            }

            function Cond1(%s): bool {
              %s
            }

            function Cond2(%s): bool {
              %s
            }
            """
                .formatted(
                        variables, paramNames, paramNames, // ensures
                        variables, paramNames, paramNames, // forall block
                        paramNames, paramNames, paramNames, paramNames, // body
                        variables, cond1, // Cond1 def
                        variables, cond2  // Cond2 def
                );
    }

    private String extractParamNames(String declaration) {
        return Arrays.stream(declaration.split(","))
                .map(String::trim)
                .map(s -> s.split(":")[0].trim())
                .collect(Collectors.joining(", "));
    }

    public Path write(String content) throws IOException {
        Path file = Files.createTempFile("equiv-check", ".dfy");
        Files.writeString(file, content);
        return file;
    }

    public boolean verify(Path dafnyFile) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "dotnet",
                "C:\\Users\\Tomas Maciel\\.vscode\\extensions\\dafny-lang.ide-vscode-3.4.4\\out\\resources\\4.10.0\\github\\dafny\\Dafny.dll",
                "verify",
                dafnyFile.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output = new String(process.getInputStream().readAllBytes());
        process.waitFor();

        return output.contains("verified, 0 errors");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Evaluation evaluation = new Evaluation();
        JsonReader jsonReader = new JsonReader();
        List<CodeTask> listCode = jsonReader.readJsonlFile("src/main/java/com/tomas/evaluation/mbpp.jsonl");
        for (CodeTask codeTask : listCode) {
            evaluation.evaluateIndividual(codeTask);
        }
        System.out.println("Consistency detections: " + evaluation.counterConsistencyDetections);
        System.out.println("Total evaluations: " + evaluation.totalCounter);
        System.out.println("Correct code evaluations: " + evaluation.counterCorrectCode);
        System.out.println("Correct specs evaluations: " + evaluation.counterCorrectSpecs);
    }
}

