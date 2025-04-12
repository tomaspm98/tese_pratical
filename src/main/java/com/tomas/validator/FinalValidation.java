package com.tomas.validator;

import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


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
        restTemplate.postForObject("http://localhost:8080/code-generator", message, String.class);
        int counter = 0;
        String postcondition = dafnyTranslator.getSpecs().get("postcondition");
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("python");
        for (Map<String, Integer> input : inputsFromAlloy) {
            String postcondition_replaced = postcondition;
            String output = codeRunner.getOutputFromCode(input);
            for (Map.Entry<String, Integer> entry : input.entrySet()) {
                postcondition_replaced = postcondition_replaced.replace(entry.getKey(), entry.getValue().toString());
            }
            postcondition_replaced = postcondition_replaced.replaceAll("[a-zA-Z]+", output);
            postcondition_replaced = postcondition_replaced.replaceAll(";$", "");
            postcondition_replaced = postcondition_replaced.replaceAll("\\s*&&\\s*\n", "and");
            postcondition_replaced = postcondition_replaced.replaceAll("\\s*\\|\\|\\s*", "or");
            if ((boolean) engine.eval(postcondition_replaced)) {
                counter++;
            }
        }
        return (double) counter / inputsFromAlloy.size();
    }

}


