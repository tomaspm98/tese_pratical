package com.tomas.validator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyToAlloyConverter {

    private final DafnyTranslator dafnyTranslator;

    public DafnyToAlloyConverter(DafnyTranslator dafnyTranslator) {
        this.dafnyTranslator = dafnyTranslator;
    }

    public String constructPrecondition(Map<String, List<String>> specs, List<String> inputVars) {
        List<String> preconditionList = specs.get("precondition");
        String precondition = dafnyTranslator.constructOneCondition(preconditionList);
        if (precondition == null || precondition.isEmpty() || precondition.equals("( true )") || precondition.equals("( true; )") || precondition.equals("( true  )")) //last case when there is a "requires true //comment"
        {
            return "1=1";
        }

        for (String var : inputVars) {
            precondition = precondition.replaceAll("\\b" + var + "\\b", "input." + var);
        }
        precondition = precondition.replaceAll("(\\b[\\w.]+)\\.Length", "#$1");
        precondition = precondition.replaceAll("\\|(.+?)\\|", "#$1");
        precondition = precondition.replaceAll("input\\.(\\w+)(\\s*!=\\s*\\[\\s*])", "some input.$1");
        precondition = precondition.replaceAll("(\\w+(?:\\.\\w+)*)\\s*!=\\s*null", "some $1");
        precondition = precondition.replaceAll("(\\w+(?:\\.\\w+)*)\\s*!=\\s*\"\"", "some $1");
        precondition = precondition.replaceAll("(?<![=!])==(?=\\s)", "="); // Em Alloy, == é =
        if (precondition.contains("forall")) {
            Pattern pattern = Pattern.compile("forall\\s+([^:]+)::");
            Matcher matcher = pattern.matcher(precondition);
            if (matcher.find()) {
                String variables = matcher.group(1);
                if (variables.contains(",")) {
                    precondition = transformComplexForallDafny(precondition);
                } else {
                    precondition = transformSimpleForallDafny(precondition);
                }
            }
        }
        if (precondition.contains("exists")) {
            Pattern pattern = Pattern.compile("exists\\s+([^:]+)::");
            Matcher matcher = pattern.matcher(precondition);
            if (matcher.find()) {
                String variables = matcher.group(1);
                if (variables.contains(",")) {
                    precondition = transformComplexExistsDafny(precondition);
                } else {
                    precondition = transformSimpleExistsDafny(precondition);
                }
            }
        }
        return precondition;
    }

    public String transformSimpleForallDafny(String input) {
        String pattern = "forall\\s+(\\w+)\\s*::\\s*(\\d+)\\s*<=\\s*\\1\\s*<\\s*([^=]+)==>\\s*(.+)";
        return input.replaceAll(pattern,
                "all $1: Int | $1 >= $2 and $1 < $3 => $4");
    }

    public String transformComplexForallDafny(String input) {
        String pattern = "forall\\s+(\\w+)\\s*,\\s*(\\w+)\\s*::\\s*(\\d+)\\s*<=\\s*\\1\\s*<\\s*\\2\\s*<\\s*([^=]+)==>\\s*(.+)";
        return input.replaceAll(pattern,
                "all $1: Int | all $2: Int | $1 >= $3 and $1 < $2 and $2 < $4 => $5");
    }

    public String transformSimpleExistsDafny(String input) {
        String pattern = "exists\\s+(\\w+)\\s*::\\s*(\\d+)\\s*<=\\s*\\1\\s*<\\s*([^=]+)==>\\s*(.+)";
        return input.replaceAll(pattern,
                "some $1: Int | $1 >= $2 and $1 < $3 => $4");
    }

    public String transformComplexExistsDafny(String input) {
        String pattern = "exists\\s+(\\w+)\\s*,\\s*(\\w+)\\s*::\\s*(\\d+)\\s*<=\\s*\\1\\s*<\\s*\\2\\s*<\\s*([^=]+)==>\\s*(.+)";
        return input.replaceAll(pattern,
                "some $1: Int | all $2: Int | $1 >= $3 and $1 < $2 and $2 < $4 => $5");
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
        } else {
            Matcher matcherWithoutReturns = Pattern.compile("method\\s+\\w+\\(([^)]*)\\)").matcher(expression);
            if (matcherWithoutReturns.find()) {
                String parameters = matcherWithoutReturns.group(1);
                Matcher varMatcher = Pattern.compile("(\\w+)\\s*:\\s*\\w+").matcher(parameters);
                while (varMatcher.find()) {
                    inputVars.add(varMatcher.group(1));
                }
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
        Map<String, List<String>> dafnySpecs = dafnyTranslator.extractSpecs(code);
        Map<String, String> paramsWithType = dafnyTranslator.parseParamsWithType(dafnyTranslator.extractVariables(methodSignature));
        boolean isString = false;
        boolean isBool = false;

        Map<String, List<String>> variables = extractVariables(methodSignature);
        String methodName = extractMethodName(methodSignature);

        String runCommand = "run {} for 1 but 10 Int";

        List<String> inputVars = variables.get("input");
        StringBuilder inputSig = new StringBuilder("sig Input {");
        for (Map.Entry<String, String> entry : paramsWithType.entrySet()) {
            switch (entry.getValue()) {
                case " int", " real", " nat":
                    inputSig.append("\n    ").append(entry.getKey()).append(": Int,");
                    break;
                case " array<int>", " seq<int>":
                    inputSig.append("\n    ").append(entry.getKey()).append(": seq Int,");
                    runCommand = "run {} for 4 seq, 9 Int";
                    break;
                case " string", " str", " seq<char>", " array<char>":
                    inputSig.append("\n    ").append(entry.getKey()).append(": seq Char,");
                    runCommand = "run {} for 4 seq, 9 Int";
                    isString = true;
                    break;
                case " array<bool>", " seq<bool>":
                    inputSig.append("\n    ").append(entry.getKey()).append(": seq Bool,");
                    runCommand = "run {} for 4 seq, 9 Int";
                    isBool = true;
                    break;
                default:
                    inputSig.append("\n    ").append(entry.getKey()).append(": ").append(entry.getValue()).append(",");
                    break;
            }
        }
        inputSig.setLength(inputSig.length() - 1);
        inputSig.append("\n}\n");

        String precondition = constructPrecondition(dafnySpecs, inputVars);

        if (isString) {
            runCommand = """
                    abstract sig Char {}
                    one sig A, B, C, D0, E1, F2, Z extends Char {}
                    
                    
                    run {} for 4 seq, 9 Int
                    """;
        }

        if (isBool) {
            return String.format("""
                    module %s
                    
                    open util/boolean
                    
                    %s
                    
                    fact Preconditions {
                        all input: Input | %s
                    }
                    
                    fact {
                        some Input
                    }
                    
                    %s
                    """, methodName, inputSig, precondition, runCommand);
        } else {
            return String.format("""
                    module %s
                    
                    %s
                    
                    fact Preconditions {
                        all input: Input | %s
                    }
                    
                    fact {
                        some Input
                    }
                    
                    %s
                    """, methodName, inputSig, precondition, runCommand);
        }
    }
}
