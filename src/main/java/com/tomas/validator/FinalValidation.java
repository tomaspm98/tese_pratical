package com.tomas.validator;

import com.tomas.util.NullOutputException;
import com.tomas.util.Util;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public double conditionParser(Set<Map<String, Integer>> inputsFromAlloy, String message) throws IOException, ScriptException {
        final int MAX_RETRIES = 3;
        int retries = 0;
        while (retries < MAX_RETRIES) {
            restTemplate.postForObject("http://localhost:8080/code-generator", message, String.class);
            int counter = 0;
            boolean allOutputsValid = true;
            String postcondition = dafnyTranslator.getSpecs().get("postcondition");
            String outputVarName = getOutputVariableName().get(0);
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("python");
            for (Map<String, Integer> input : inputsFromAlloy) {
                String postcondition_replaced = postcondition;
                String output = codeRunner.getOutputFromCode(input);
                if (output == null) {
                    allOutputsValid = false;
                    break; // exit early to retry
                }
                for (Map.Entry<String, Integer> entry : input.entrySet()) {
                    postcondition_replaced = postcondition_replaced.replaceAll(
                            "(?<![a-zA-Z0-9_])" + entry.getKey() + "(?![a-zA-Z0-9_])",
                            entry.getValue().toString()
                    );
                }
                postcondition_replaced = postcondition_replaced.replaceAll(outputVarName, output);
                postcondition_replaced = postcondition_replaced.replaceAll(";$", "");
                postcondition_replaced = postcondition_replaced.replaceAll("\\s*&&\\s*\n", "and");
                postcondition_replaced = postcondition_replaced.replaceAll("\\s*\\|\\|\\s*", "or");
                if ((boolean) engine.eval(postcondition_replaced)) {
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

}


