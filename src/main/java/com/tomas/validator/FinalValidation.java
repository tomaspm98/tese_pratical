package com.tomas.validator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

public class FinalValidation {

    private Map<String, String> specs;

    public FinalValidation(Map<String, String> specs) {
        this.specs = specs;
    }

    public String conditionParser(List<Map<String, Integer>> inputsFromAlloy, int output) {
        String postcondition = specs.get("postcondition");
        for (Map<String, Integer> input : inputsFromAlloy) {
            for (Map.Entry<String, Integer> entry : input.entrySet()) {
                postcondition = postcondition.replace(entry.getKey(), entry.getValue().toString());
            }
        }
        postcondition = postcondition.replaceAll("[a-zA-Z]+", String.valueOf(output));
        return postcondition;
    }

    public boolean validatePostcondition(String expression) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

        return (boolean) engine.eval(expression);
    }

}


