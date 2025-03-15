package com.tomas.validator;

import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class FinalValidation {

    private final DafnyTranslator dafnyTranslator;
    private final CodeRunner codeRunner;
    private final RestTemplate restTemplate;

    public FinalValidation(CodeRunner codeRunner, DafnyTranslator DafnyTranslator, RestTemplate restTemplate) {
        this.codeRunner = codeRunner;
        this.dafnyTranslator = DafnyTranslator;
        this.restTemplate = restTemplate;
    }

    public double conditionParser(Set<Map<String, Integer>> inputsFromAlloy, String message) throws IOException, ScriptException {
        restTemplate.postForObject("http://localhost:8080/code-generator", message, String.class);
        int counter = 0;
        String postcondition = dafnyTranslator.getSpecs().get("postcondition");
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        int output;
        for (Map<String, Integer> input : inputsFromAlloy) {
            output = codeRunner.getOutputFromCode(input);
            for (Map.Entry<String, Integer> entry : input.entrySet()) {
                postcondition = postcondition.replace(entry.getKey(), entry.getValue().toString());
            }
            postcondition = postcondition.replaceAll("[a-zA-Z]+", String.valueOf(output));
            if ((boolean) engine.eval(postcondition)) {
                counter++;
            }
        }
        return (double) counter / inputsFromAlloy.size();
    }

}


