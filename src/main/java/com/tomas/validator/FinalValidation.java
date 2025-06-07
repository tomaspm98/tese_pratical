package com.tomas.validator;

import com.tomas.util.NullOutputException;
import com.tomas.util.Util;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        int retriesCode = 0;
        boolean realOutput = false;
        boolean pairOutput = false;
        List<String> postconditionsProcessed = new ArrayList<>();
        while (retriesCode < MAX_RETRIES) {
            restTemplate.postForObject("http://localhost:8080/code-generator", message, String.class);
            int counter = 0;
            boolean allOutputsValid = true;
            List<String> postcondition = dafnyTranslator.getSpecs().get("postcondition");
            List<String> outputVarNames = getOutputVariableName();
            for (Map<String, Object> input : inputsFromAlloy) {
                String output = codeRunner.getOutputFromCode(input);
                if (output == null) {
                    allOutputsValid = false;
                    break;
                }
                if (output.contains("(") && output.contains(")") && !output.contains("Traceback")) {
                    pairOutput = true;
                }
                String postcondition_replaced;
                if (pairOutput) {
                    String processedOutput = output.replaceAll("[()]", "");
                    String[] outputs = processedOutput.split(",");
                    for (String singlePostcondition: postcondition) {
                        for (int i = 0; i < outputs.length; i++) {
                            if (outputVarNames!=null && singlePostcondition.contains(outputVarNames.get(i))) {
                                if (outputs[i].contains(".")) {
                                    realOutput = true;
                                    singlePostcondition= transformDafnyCondition(singlePostcondition);
                                    singlePostcondition = singlePostcondition.replaceAll(outputVarNames.get(i), outputs[i]);
                                } else {
                                    if (outputs[i].equals("False")) {
                                        outputs[i] = "false";
                                    } else if (outputs[i].equals("True")) {
                                        outputs[i] = "true";
                                    }
                                    singlePostcondition = singlePostcondition.replaceAll(outputVarNames.get(i), outputs[i]);
                                }
                            }
                        }
                        postconditionsProcessed.add(singlePostcondition);
                    }
                    postcondition_replaced = dafnyTranslator.constructOneCondition(postconditionsProcessed);
                } else {
                    if (output.equals("False")) {
                        output = "false";
                    } else if (output.equals("True")) {
                        output = "true";
                    }
                    postcondition_replaced = dafnyTranslator.constructOneCondition(postcondition);
                    if (outputVarNames!=null && !outputVarNames.isEmpty()) {
                        postcondition_replaced = postcondition_replaced.replaceAll(outputVarNames.getFirst(), output);
                    }
                }
                for (Map.Entry<String, Object> entry : input.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        postcondition_replaced = postcondition_replaced.replaceAll(
                                "(?<![a-zA-Z0-9_])" + entry.getKey() + "(?![a-zA-Z0-9_])",
                                "\"" + entry.getValue() + "\""
                        );
                    } else {
                        postcondition_replaced = postcondition_replaced.replaceAll(
                                "(?<![a-zA-Z0-9_])" + entry.getKey() + "(?![a-zA-Z0-9_])",
                                entry.getValue().toString()
                        );
                    }
                }
                postcondition_replaced = postcondition_replaced.replaceAll(";$", "");
                postcondition_replaced = postcondition_replaced.replaceAll("(\\[[^\\[\\]]+])\\.Length", "|$1|");
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
            retriesCode++;
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
}


