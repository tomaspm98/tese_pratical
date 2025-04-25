package com.tomas.validator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyToAlloyConverter {

    private final DafnyTranslator dafnyTranslator;

    public DafnyToAlloyConverter(DafnyTranslator dafnyTranslator) {
        this.dafnyTranslator = dafnyTranslator;
    }

    public String constructPrecondition(Map<String, String> specs, List<String> inputVars) {
        String precondition = specs.get("precondition");
        if (precondition == null || precondition.equals("true") || precondition.equals("true;")) {
            return "1=1";
        }

        for (String var : inputVars) {
            precondition = precondition.replaceAll("\\b" + var + "\\b", "i." + var);
        }
        precondition = precondition.replaceAll(";", "");
        precondition = precondition.replaceAll("//.*", "");
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
        postcondition = postcondition.replaceAll(";", "");
        postcondition = postcondition.replaceAll("//.*", "");
        return postcondition;
    }

    public Map<String, String> constructCheckEvaluationInput(String original, String evaluation, List<String> inputVars) {
        StringBuilder assertion = new StringBuilder();
        int random = (int) (Math.random()*1000);
        for (String var : inputVars) {
            original = original.replaceAll("\\b" + var + "\\b", "i." + var);
            evaluation = evaluation.replaceAll("\\b" + var + "\\b", "i." + var);
        }
        original = original.replaceAll( "==", "=");
        evaluation = evaluation.replaceAll("==", "=");
        original = original.replaceAll( ";", "");
        evaluation = evaluation.replaceAll(";", "");

        assertion.append("assert ").append("assertion_").append(random).append(" {\n");
        assertion.append("    ").append("all i:Input | ").append(original).append(" <=> ").append(evaluation).append("\n");
        assertion.append("}\n");

        String checkAssertion = "check assertion_" + random + " for 70 but 6 Int\n";
        return Map.of("assertion", assertion.toString(), "check", checkAssertion);
    }

    public Map<String, String> constructCheckEvaluationOutput(String original, String evaluation, List<String> outputVars, List<String> inputVars) {
        StringBuilder assertion = new StringBuilder();
        int random = (int) (Math.random()*1000);
        for (String var : outputVars) {
            original = original.replaceAll("\\b" + var + "\\b", "o." + var);
            evaluation = evaluation.replaceAll("\\b" + var + "\\b", "o." + var);
        }

        for (String var : inputVars) {
            original = original.replaceAll("\\b" + var + "\\b", "i." + var);
            evaluation = evaluation.replaceAll("\\b" + var + "\\b", "i." + var);
        }

        String finalOriginal = original;
        String finalEvaluation = evaluation;
        boolean hasInputVars = inputVars.stream().anyMatch(var ->
                finalOriginal.contains("i." + var) || finalEvaluation.contains("i." + var)
        );

        original = original.replaceAll( "==", "=");
        evaluation = evaluation.replaceAll("==", "=");
        original = original.replaceAll( ";", "");
        evaluation = evaluation.replaceAll(";", "");


        assertion.append("assert ").append("assertion_").append(random).append(" {\n").append("    ");
        if (hasInputVars){
            assertion.append("all o:Output, i:Input | ").append(original).append(" <=> ").append(evaluation).append("\n");
        } else {
            assertion.append("all o:Output | ").append(original).append(" <=> ").append(evaluation).append("\n");
        }
        assertion.append("}\n");

        String checkAssertion = "check assertion_" + random + " for 70 but 6 Int\n";
        return Map.of("assertion", assertion.toString(), "check", checkAssertion);
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

    public String convertToAlloyRun(String code) {
        String methodSignature = dafnyTranslator.extractMethodSignature(code);
        Map<String, String> dafnySpecs = dafnyTranslator.extractSpecs(code);

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

        String precondition = constructPrecondition(dafnySpecs, inputVars);
        String postcondition = constructPostcondition(dafnySpecs, outputVars, inputVars);

        // Construct final Alloy model
        return String.format("""
        module %s

        %s
        
        fact Preconditions {
            all i: Input | %s
        }
        
        fact {
            some Input
        }

        run {} for 1 but 10 Int
        """, methodName, inputSig, precondition, postcondition);
    }

    public String convertToAlloyCheck(String code) {
        String methodSignature = dafnyTranslator.extractMethodSignature(code);

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

        // Construct final Alloy model
        return String.format("""
        module %s

        %s
        %s
        
        

        """, methodName, inputSig, outputSig);
    }

}
