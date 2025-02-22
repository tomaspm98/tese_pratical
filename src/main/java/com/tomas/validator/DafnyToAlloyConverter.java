package com.tomas.validator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyToAlloyConverter {

    public String constructPrecondition(Map<String, String> specs, List<String> inputVars) {
        String precondition = specs.get("precondition");
        for (String var : inputVars) {
            precondition = precondition.replaceAll("\\b" + var + "\\b", "i." + var);
        }
        return precondition;
    }

    public String constructPostcondition(Map<String, String> specs, List<String> outputVars, List<String> inputVars) {
        String postcondition = specs.get("postcondition");
        for (String var : outputVars) {
            postcondition = postcondition.replaceAll("\\b" + var + "\\b", "o." + var);
        }
        for (String var : inputVars) {
            postcondition = postcondition.replaceAll("\\b" + var + "\\b", "i." + var);
        }
        return postcondition;
    }

    private List<String> extractInputVariables(String expression){
        List<String> inputVars = new ArrayList<>();
        Matcher matcher = Pattern.compile("method\\s+\\w+\\(([^)]*)\\) ").matcher(expression);
        if (matcher.find()){
            String parameters = matcher.group(1);
            Matcher varMatcher = Pattern.compile("(\\w+)\\s*:\\s*\\w+").matcher(parameters);
            while (varMatcher.find()) {
                inputVars.add(varMatcher.group(1));
            }
        }
        return inputVars;
    }

    private List<String> extractOutputVariables(String expression){
        List<String> outputVars = new ArrayList<>();
        Matcher requiresMatcher = Pattern.compile("returns\\s*\\(([^)]+)\\)").matcher(expression);
        if (requiresMatcher.find()){
            String parameters = requiresMatcher.group(1);
            Matcher varMatcher = Pattern.compile("(\\w+)\\s*:\\s*\\w+").matcher(parameters);
            while (varMatcher.find()) {
                outputVars.add(varMatcher.group(1));
            }
        }
        return outputVars;
    }

    private Map<String,List<String>> extractVariables(String expression) {
        Map<String, List<String>> variables = new HashMap<>();
        List<String> inputVars = extractInputVariables(expression);
        variables.put("input", inputVars);

        List<String> outputVars = extractOutputVariables(expression);
        variables.put("output", outputVars);
        return variables;
    }

    private String extractMethodName(String expression) {
        Matcher matcher = Pattern.compile("method\\s+(\\w+)\\(").matcher(expression);
        if (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    public String convertToAlloy(String methodSignature, Map<String, String> dafnySpecs) {

        Map<String, List<String>> variables = extractVariables(methodSignature);
        String methodName = extractMethodName(methodSignature);

        List<String> inputVars = variables.get("input");
        List<String> outputVars = variables.get("output");
        StringBuilder inputSig = new StringBuilder("sig Input {");
        for (String var : inputVars) {
            inputSig.append("\n    ").append(var).append(": Int,"); //podemos ir buscar o tipo da variavel a assinatura do metodo, e ter um converter de tipos de dafny para alloy
        }
        inputSig.setLength(inputSig.length() - 1);
        inputSig.append("\n}\n");

        StringBuilder outputSig = new StringBuilder("sig Output {");
        for (String var : outputVars) {
            outputSig.append("\n    ").append(var).append(": Int,");
        }
        outputSig.setLength(outputSig.length() - 1);
        outputSig.append("\n}\n");

        String precondition = constructPrecondition(dafnySpecs, inputVars);
        String postcondition = constructPostcondition(dafnySpecs, outputVars, inputVars);

        // Construct final Alloy model
        return String.format("""
        module %s

        %s
        %s

        fact Preconditions {
            all i: Input | %s
        }

        fact Postconditions {
            all i: Input, o: Output | %s
        }

        run {} for 50
        """, methodName, inputSig, outputSig, precondition, postcondition);
    }

    public static void main(String[] args) {
        DafnyToAlloyConverter converter = new DafnyToAlloyConverter();
        String code = "method Add(a: int, b: int) returns (result: int)\n" +
                "  requires a != 0 || b != 0\n" +
                "  ensures result == a + b\n" +
                "{\n" +
                "  result := a + b;\n" +
                "}";
        Map<String, String> specs = new DafnyTranslator().extractSpecs(code);
        String methodSignature = new DafnyTranslator().extractMethodSignature(code);
        System.out.println(converter.convertToAlloy(methodSignature, specs));
    }

}
