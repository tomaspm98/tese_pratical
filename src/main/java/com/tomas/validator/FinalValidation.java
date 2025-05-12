package com.tomas.validator;

import com.tomas.util.NullOutputException;
import com.tomas.util.Util;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FinalValidation {

    private final DafnyTranslator dafnyTranslator;
    private final CodeRunner codeRunner;
    private final RestTemplate restTemplate;

    public FinalValidation(CodeRunner codeRunner, DafnyTranslator dafnyTranslator, RestTemplate restTemplate) {
        this.codeRunner = codeRunner;
        this.restTemplate = restTemplate;
        this.dafnyTranslator = dafnyTranslator;
    }

    public double conditionParser(Set<Map<String, Object>> inputsFromAlloy, String message) throws IOException, InterruptedException {
        final int MAX_RETRIES = 3;
        int retries = 0;
        boolean realOutput = false;
        while (retries < MAX_RETRIES) {
            restTemplate.postForObject("http://localhost:8080/code-generator", message, String.class);
            int counter = 0;
            boolean allOutputsValid = true;
            String postcondition = dafnyTranslator.getSpecs().get("postcondition");
            String outputVarName = getOutputVariableName().get(0);
            for (Map<String, Object> input : inputsFromAlloy) {
                String postcondition_replaced = postcondition;
                String output = codeRunner.getOutputFromCode(input);
                if (output == null) {
                    allOutputsValid = false;
                    break;
                }
                if (output.contains(".")) {
                    realOutput = true;
                    postcondition_replaced = transformDafnyCondition(postcondition_replaced);
                }
                if (output .equals("False")) {
                    output = "false";
                } else if (output.equals("True")) {
                    output = "true";
                }
                for (Map.Entry<String, Object> entry : input.entrySet()) {
                    postcondition_replaced = postcondition_replaced.replaceAll(
                            "(?<![a-zA-Z0-9_])" + entry.getKey() + "(?![a-zA-Z0-9_])",
                            entry.getValue().toString()
                    );
                }
                postcondition_replaced = postcondition_replaced.replaceAll(outputVarName, output);
                postcondition_replaced = postcondition_replaced.replaceAll(";$", "");
                String dafnyCode;
                if (realOutput) {
                    dafnyCode = "function convertToReal(x: int): real {\n  x as real\n}\nmethod Check() {\n  assert " + postcondition_replaced + ";\n}";
                } else {
                    dafnyCode = "method Check() {\n    assert " + postcondition_replaced + ";\n}";
                }
                Path file = Files.createTempFile("equiv-check", ".dfy");
                Files.writeString(file, dafnyCode);
                if (verify(file)) {
                    counter++;
                }
            }
            if (allOutputsValid) {
                return (double) counter / inputsFromAlloy.size();
            }
            retries++;
        }
        System.out.println("Output was null after " + MAX_RETRIES + " retries.");
        throw new NullOutputException("Output was null after " + MAX_RETRIES + " retries.");
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

        return output.contains("1 verified, 0 errors");
    }


    public List<String> getOutputVariableName() {
        List<String> returns = null;
        String methodSignature = dafnyTranslator.getMethodSignature();
        Matcher matcher = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignature);
        if (matcher.find()) {
            String returnsRaw = matcher.group(2);

            returns = Util.extractVariableNames(returnsRaw);
        }

        return returns;
    }

    public String transformDafnyCondition(String condition) {
        List<String> validOperators = List.of("==", "!=", "<=", ">=", "<", ">");
        for (String operator : validOperators) {
            int index = condition.indexOf(operator);
            if (index != -1) {
                String lhs = condition.substring(0, index + operator.length());
                String rhs = condition.substring(index + operator.length());
                return lhs + " convertToReal( " + rhs + " )";
            }
        }
        return condition;
    }

    public static void main(String[] args) {
        String condition = "volume <= a * b * h / 2;";
        FinalValidation finalValidation = new FinalValidation(new CodeRunner(), new DafnyTranslator(), new RestTemplate());
        String transformedCondition = finalValidation.transformDafnyCondition(condition);
        System.out.println("Transformed condition: " + transformedCondition);


    }

}


