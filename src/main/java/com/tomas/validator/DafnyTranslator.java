package com.tomas.validator;

import com.tomas.util.Util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyTranslator {

    private Map<String, String> specs = new HashMap<>();
    private String methodSignature;

    public Map<String, String> extractSpecs(String code) {
        specs = new HashMap<>();
        List<String> preconditions = new ArrayList<>();
        List<String> postconditions = new ArrayList<>();

        Matcher requiresMatcher = Pattern.compile("requires (.+)").matcher(code);
        Matcher ensuresMatcher = Pattern.compile("ensures (.+)").matcher(code);

        while (requiresMatcher.find()) {
            String precondition = requiresMatcher.group(1).trim();
            precondition = precondition.replaceAll(";", "");
            precondition = precondition.replaceAll("//.*", "");
            preconditions.add(precondition);
        }

        while (ensuresMatcher.find()) {
            String postcondition = ensuresMatcher.group(1).trim();
            postcondition = postcondition.replaceAll(";", "");
            postcondition = postcondition.replaceAll("//.*", "");
            postconditions.add(postcondition);
        }
        specs.put("precondition", constructOneCondition(preconditions));
        specs.put("postcondition", constructOneCondition(postconditions));

        return specs;
    }

    private String constructOneCondition(List<String> conditions) {
        StringBuilder condition = new StringBuilder();
        for (String cond : conditions) {
            if (!condition.isEmpty()) {
                condition.append(" && ");
            }
            condition.append(cond);
        }
        return condition.toString();
    }

    public String extractMethodSignature (String code) {
        Pattern methodPattern = Pattern.compile("(method\\s+.+)");
        Matcher methodMatcher = methodPattern.matcher(code);
        if (methodMatcher.find()) {
            methodSignature = methodMatcher.group(1).trim();
            return methodMatcher.group(1).trim();
        }
        return null;
    }

    public String extractVariables(String expression) {
        Matcher matcher = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(expression);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public Map<String, String> parseParamsWithType(String input) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = input.split(",");

        for (String pair : pairs) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];
                map.put(key, value);
            }
        }

        return map;
    }

    public Map<String, String> getSpecs() {
        return specs;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public static void main(String[] args) {
        String dafnyCode = """
                method test(a: int, b: int) returns (c: int)
                """;

        DafnyTranslator translator = new DafnyTranslator();
        String specs = translator.extractVariables(dafnyCode);
        String methodSignature = translator.extractMethodSignature(dafnyCode);

        System.out.println("Specs: " + specs);
        System.out.println("Method Signature: " + methodSignature);
    }

}
