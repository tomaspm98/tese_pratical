package com.tomas.evaluation;

import com.tomas.validator.DafnyToAlloyConverter;
import com.tomas.validator.DafnyTranslator;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import kodkod.engine.satlab.SATFactory;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluation {

    DafnyTranslator dafnyTranslator = new DafnyTranslator();
    DafnyToAlloyConverter dafnyToAlloyConverter = new DafnyToAlloyConverter(dafnyTranslator);

    public boolean evaluateIndividual(CodeTask codeTask) throws IOException, InterruptedException {
        /*RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/input";

        String message = codeTask.getText();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<InputResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, InputResponse.class);

        String codeCleaned = response.getBody().getCodeGenerated().replaceAll("(?s)def main\\(\\):.*", "");
        String specs = response.getBody().getSpecsGenerated();
        */String codeCleaned = "import sys\n" +
                "\n" +
                "def find_perimeter(side_length):\n" +
                "    return 4 * side_length\n";
        String specs = "```dafny\n" +
                "method findPerimeterOfSquare(side: int) returns (perimeter: int)\n" +
                "  requires side >= 0\n" +
                "  ensures perimeter == 4 * side\n" +
                "{\n" +
                "  perimeter := 4 * side;\n" +
                "}\n" +
                "```";

        evaluateSpecs(specs, codeTask.getTask_id());
        evaluateCode(codeCleaned, codeTask.getTest_list());

        return false;
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

    public boolean evaluateSpecs(String specs, int task_id) throws IOException {
        Map<String, String> specsConditions = dafnyTranslator.extractSpecs(specs);
        String methodSignature = dafnyTranslator.extractMethodSignature(specs);
        List<String> params = new ArrayList<>();
        List<String> returns = new ArrayList<>();

        Matcher matcher = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignature);
        if (matcher.find()) {
            String paramsRaw = matcher.group(1);
            String returnsRaw = matcher.group(2);

            params = extractVariableNames(paramsRaw);
            returns = extractVariableNames(returnsRaw);
        }

        String taskIdFile = "task_id_" + task_id + ".dfy";
        String codeInFile = Files.readString(Path.of("src/main/java/com/tomas/evaluation/dafny/" + taskIdFile));

        Map<String, String> specsConditionsFile = dafnyTranslator.extractSpecs(codeInFile);
        String methodSignatureFile = dafnyTranslator.extractMethodSignature(codeInFile);

        Matcher matcherFile = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignatureFile);
        if (matcherFile.find()) {
            String paramsRaw = matcherFile.group(1);
            String returnsRaw = matcherFile.group(2);

            List<String> paramsFile = extractVariableNames(paramsRaw);
            List<String> returnsFile = extractVariableNames(returnsRaw);

            for (int i = 0; i < paramsFile.size(); i++) {
                String newPrecondition = specsConditionsFile.get("precondition").replaceAll(paramsFile.get(i), params.get(i));
                specsConditionsFile.put("precondition", newPrecondition);
            }
            for (int i =0; i<returnsFile.size(); i++) {
                String newPostcondition = specsConditionsFile.get("postcondition").replaceAll(returnsFile.get(i), returns.get(i));
                specsConditionsFile.put("postcondition", newPostcondition);
            }
        }

        /*List<Map<String, String>> checks = new ArrayList<>();
        for (String key : specsConditions.keySet()) {
            if (key.equals("precondition")) {
                checks.add(dafnyToAlloyConverter.constructCheckEvaluationInput(specsConditions.get(key), specsConditionsFile.get(key), params));
            } else if (key.equals("postcondition")) {
                checks.add(dafnyToAlloyConverter.constructCheckEvaluationOutput(specsConditions.get(key), specsConditionsFile.get(key), returns));
            }
        }*/
        EvalUtilities eval = new EvalUtilities();
        IExpr expr1 = eval.evaluate(specsConditions.get("postcondition"));
        IExpr expr2 = eval.evaluate(specsConditionsFile.get("postcondition"));
        if (expr1.equals(expr2)) {
            System.out.println("Precondition is valid");
        } else {
            System.out.println("Precondition is invalid");
        }




        return false;
    }

    /*public boolean runAlloyCheck(String code, List<Map<String, String>> checks) {
        StringBuilder alloyModel = new StringBuilder(dafnyToAlloyConverter.convertToAlloyCheck(code));
        for (Map<String, String> check : checks) {
            alloyModel.append(check.get("assertion")).append("\n");
        }
        for (Map<String, String> check : checks) {
            alloyModel.append(check.get("check"));
        }

        boolean result = false;

        try {
            A4Reporter rep = new A4Reporter();
            CompModule world = CompUtil.parseEverything_fromString(rep, alloyModel.toString());
            A4Options options = new A4Options();
            options.solver = SATFactory.get("minisat");

            // Get the check command (instead of a run command)
            Command cmd = world.getAllCommands().get(0);

            // Execute the check command
            A4Solution solution = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd, options);

            // If the check is satisfiable, it means the check holds (i.e., it is true)
            // The check failed (the property doesn't hold)
            result = solution.satisfiable();  // The check passed (the property holds)

        } catch (Err e) {
            System.err.println("Alloy error: " + e.getMessage());
        }

        return result;
    }*/

    public static List<String> extractVariableNames(String input) {
        List<String> names = new ArrayList<>();

        if (input.trim().isEmpty()) return names;  // Return empty list if no content

        String[] parts = input.split(",");

        for (String part : parts) {
            String[] pair = part.trim().split(":");
            if (pair.length > 0) {
                names.add(pair[0].trim());
            }
        }
        return names;
    }

    public String getMethodName(String code) {
        Pattern pattern = Pattern.compile("(?<=def\\s)(\\w+)(?=\\()");
        Matcher matcher = pattern.matcher(code);

        String methodName = "";
        if (matcher.find()) {
            methodName = matcher.group(1); // Capture group 1 contains the method name
        }

        return methodName;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        JsonReader jsonReader = new JsonReader();
        List<CodeTask> listCode = jsonReader.readJsonlFile("src/main/java/com/tomas/evaluation/mbpp.jsonl");
        Evaluation evaluation = new Evaluation();
        boolean eval = evaluation.evaluateIndividual(listCode.getFirst());
    }
}
