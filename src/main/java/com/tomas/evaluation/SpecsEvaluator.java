package com.tomas.evaluation;

import com.tomas.util.Util;
import com.tomas.validator.DafnyTranslator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpecsEvaluator {

    DafnyTranslator dafnyTranslator = new DafnyTranslator();

    public boolean evaluateSpecs(String specs, int task_id) throws IOException, InterruptedException {
        Map<String, String> specsConditions = dafnyTranslator.extractSpecs(specs);
        if (!specsConditions.containsKey("precondition")) {
            specsConditions.put("precondition", "false");
            System.out.println("Failed to get precondition!");
        }
        if (!specsConditions.containsKey("postcondition")) {
            specsConditions.put("postcondition", "false");
            System.out.println("Failed to get postcondition!");
        }
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
                if (specsConditionsFile.get("precondition") == null || specsConditionsFile.get("precondition").isEmpty()) {
                    specsConditionsFile.put("precondition", "true");
                    break;
                }
                String newPrecondition = specsConditionsFile.get("precondition").replaceAll(paramsFile.get(i), params.get(i));
                String newPreconditionOriginal = specsConditions.get("precondition").replaceAll(";", "");
                specsConditionsFile.put("precondition", newPrecondition);
                specsConditions.put("precondition", newPreconditionOriginal);
                String newPostcondition = specsConditionsFile.get("postcondition").replaceAll("(?<!r)" + Pattern.quote(paramsFile.get(i)) + "(?!l)", params.get(i));
                specsConditionsFile.put("postcondition", newPostcondition);
            }
            for (int i = 0; i < returnsFile.size(); i++) {
                if (specsConditionsFile.get("postcondition") == null || specsConditionsFile.get("postcondition").isEmpty()) {
                    specsConditionsFile.put("postcondition", "true");
                    break;
                }
                String newPostcondition = specsConditionsFile.get("postcondition").replaceAll("(?<!r)" + Pattern.quote(returnsFile.get(i)) + "(?!l)", returns.get(i));
                newPostcondition = newPostcondition.replaceAll( ";", "");
                specsConditionsFile.put("postcondition", newPostcondition);
                String newPostconditionOriginal = specsConditions.get("postcondition").replaceAll(";", "");
                specsConditions.put("postcondition", newPostconditionOriginal);
            }
        }

        String auxiliarFunctions = extractAuxiliarFunctions(codeInFile);

        return equivalentConditions(variables, specsConditions.get("precondition"), specsConditionsFile.get("precondition"), auxiliarFunctions) && equivalentConditions(variables, specsConditions.get("postcondition"), specsConditionsFile.get("postcondition"), auxiliarFunctions);
    }

    public boolean equivalentConditions(String variables, String conditionGenerated, String conditionFile, String auxiliarFunctions) throws IOException, InterruptedException {
        String code = generate(variables, conditionGenerated, conditionFile, auxiliarFunctions);
        Path dafnyFile = write(code);

        return verify(dafnyFile);
    }

    public String generate(String variables, String cond1, String cond2, String auxiliarFunctions) {
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
            
            %s
            """
                .formatted(
                        variables, paramNames, paramNames,
                        variables, paramNames, paramNames,
                        paramNames, paramNames, paramNames, paramNames,
                        variables, cond1,
                        variables, cond2,
                        auxiliarFunctions
                );
    }

    private String extractParamNames(String declaration) {
        return Arrays.stream(declaration.split(","))
                .map(String::trim)
                .map(s -> s.split(":")[0].trim())
                .collect(Collectors.joining(", "));
    }

    public Path write(String content) throws IOException {
        Path file = Files.createTempFile("equivalencyBetweenConditions", ".dfy");
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

    public String extractAuxiliarFunctions(String codeInFile) throws IOException {
        StringBuilder result = new StringBuilder();

        Pattern functionPattern = Pattern.compile(
                "(?m)(ghost\\s+)?(function|predicate)\\s+[^\\n]*\\n(?:\\s+[^\\n]*\\n)*?\\{(?:[^{}]*|\\{[^{}]*})*}"
        );

        Matcher matcher = functionPattern.matcher(codeInFile);

        while (matcher.find()) {
            String functionBlock = matcher.group();
            functionBlock = functionBlock.replaceFirst("\\bghost\\s+", "");
            result.append(functionBlock).append("\n\n");
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        String code = """
                predicate IsDigit(c: char)
                {
                    48 <= c as int <= 57
                }
                
                
                method CountDigits(s: string) returns (count: int)
                    ensures count >= 0
                    ensures count == | set i: int | 0 <= i < |s| && IsDigit(s[i])|
                {
                    var digits := set i: int | 0 <= i < |s| && IsDigit(s[i]);
                    count := |digits|;
                }
                
                
                """;

        SpecsEvaluator specsEvaluator = new SpecsEvaluator();
        String ok = specsEvaluator.extractAuxiliarFunctions(code);
        System.out.println(ok);
    }
}
