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
        precondition = precondition.replaceAll("(\\b[\\w.]+)\\.Length", "#$1"); //transform .Length in #
        return precondition;
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
        Map<String, String> paramsWithType = dafnyTranslator.parseParamsWithType(dafnyTranslator.extractVariables(methodSignature));

        Map<String, List<String>> variables = extractVariables(methodSignature);
        String methodName = extractMethodName(methodSignature);

        String runCommand = "run {} for 1 but 10 Int";

        List<String> inputVars = variables.get("input");
        StringBuilder inputSig = new StringBuilder("sig Input {");
        for (Map.Entry<String, String> entry :  paramsWithType.entrySet()) {
            switch (entry.getValue()) {
                case " int":
                    inputSig.append("\n    ").append(entry.getKey()).append(": Int,");
                    break;
                case " array<int>":
                    inputSig.append("\n    ").append(entry.getKey()).append(": seq Int,");
                    runCommand = "run {} for 4 seq, 9 Int";
                    break;
                default:
                    inputSig.append("\n    ").append(entry.getKey()).append(": ").append(entry.getValue()).append(",");
                    break;
            }
        }
        inputSig.setLength(inputSig.length() - 1);
        inputSig.append("\n}\n");

        String precondition = constructPrecondition(dafnySpecs, inputVars);

        return String.format("""
        module %s

        %s
        
        fact Preconditions {
            all i: Input | %s
        }
        
        fact {
            some Input
        }

        %s
        """, methodName, inputSig, precondition, runCommand);
    }

    public static void main(String[] args) {
        DafnyTranslator dafnyTranslator = new DafnyTranslator();
        DafnyToAlloyConverter converter = new DafnyToAlloyConverter(dafnyTranslator);
        String dafnyCode = """
                method FindKthElement(arr: array<int>, k: int) returns (elem: int)
                  requires 0 <= k && k < arr.Length
                  ensures elem == arr[k]
                {
                    c := a + b;
                }
                """;
        String alloyModel = converter.convertToAlloyRun(dafnyCode);
        System.out.println(alloyModel);
    }

}
