package com.tomas.validator;

import com.tomas.util.Util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyTranslator {

    private Map<String, List<String>> specs = new HashMap<>();
    private String methodSignature;

    public Map<String, List<String>> extractSpecs(String code) {
        specs = new HashMap<>();
        List<String> preconditions = new ArrayList<>();
        List<String> postconditions = new ArrayList<>();

        Pattern methodPattern = Pattern.compile(
                "method\\s+(\\w+)\\s*\\([^)]*\\)\\s*returns\\s*\\([^)]*\\)([\\s\\S]*?)^\\s*\\{",
                Pattern.MULTILINE
        );

        Matcher methodMatcher = methodPattern.matcher(code);

        if (methodMatcher.find()) {

            String methodSpecs = methodMatcher.group(2);

            Matcher requiresMatcher = Pattern.compile("requires (.+)").matcher(methodSpecs);
            Matcher ensuresMatcher = Pattern.compile("ensures (.+)").matcher(methodSpecs);

            while (requiresMatcher.find()) {
                String precondition = requiresMatcher.group(1);
                precondition = precondition.replaceAll(";", "");
                precondition = precondition.replaceAll("//.*", "");
                String chainedComparison = "\\b(\\S+)\\s*(<|<=)\\s*(\\S+)\\s*(<|<=)\\s*(\\S+)\\b";
                Matcher chainedComparisonMatcher = Pattern.compile(chainedComparison).matcher(precondition);
                if (chainedComparisonMatcher.find() && !precondition.contains("forall") && !precondition.contains("exists")) {
                    precondition = normalizeComparation(precondition);
                }
                preconditions.add(precondition);
            }

            while (ensuresMatcher.find()) {
                String postcondition = ensuresMatcher.group(1);
                postcondition = postcondition.replaceAll(";", "");
                postcondition = postcondition.replaceAll("//.*", "");
                postconditions.add(postcondition);
            }
            specs.put("precondition", preconditions);
            specs.put("postcondition", postconditions);
        } else {
            Pattern methodPatternWithoutReturns = Pattern.compile(
                    "method\\s+(\\w+)\\s*\\([^)]*\\)(?!\\s*returns)([\\s\\S]*?)^\\s*\\{\n",
                    Pattern.MULTILINE
            );

            Matcher methodMatcherWithoutReturns = methodPatternWithoutReturns.matcher(code);

            if (methodMatcherWithoutReturns.find()) {

                String methodSpecs = methodMatcherWithoutReturns.group(2);

                Matcher requiresMatcher = Pattern.compile("requires (.+)").matcher(methodSpecs);
                Matcher ensuresMatcher = Pattern.compile("ensures (.+)").matcher(methodSpecs);

                while (requiresMatcher.find()) {
                    String precondition = requiresMatcher.group(1);
                    precondition = precondition.replaceAll(";", "");
                    precondition = precondition.replaceAll("//.*", "");
                    String chainedComparison = "\\b(\\S+)\\s*(<|<=)\\s*(\\S+)\\s*(<|<=)\\s*(\\S+)\\b";
                    Matcher chainedComparisonMatcher = Pattern.compile(chainedComparison).matcher(precondition);
                    if (chainedComparisonMatcher.find() && !precondition.contains("forall") && !precondition.contains("exists")) {
                        precondition = normalizeComparation(precondition);
                    }
                    preconditions.add(precondition);
                }

                while (ensuresMatcher.find()) {
                    String postcondition = ensuresMatcher.group(1);
                    postcondition = postcondition.replaceAll(";", "");
                    postcondition = postcondition.replaceAll("//.*", "");
                    postconditions.add(postcondition);
                }

                specs.put("precondition", preconditions);
                specs.put("postcondition", postconditions);
            }

        }

        return specs;
    }

    private String normalizeComparation(String expression) {
        expression = expression.replaceAll("\\s+", " ");

        String[] tokens = expression.split(" ");
        String left = tokens[0];
        String operator1 = tokens[1];
        String middle = tokens[2];
        String operator2 = tokens[3];
        String right = tokens[4];

        return String.format("%s %s %s && %s %s %s", left, operator1, middle, middle, operator2, right);
    }

    public String constructOneCondition(List<String> conditions) {
        StringBuilder condition = new StringBuilder();
        for (String cond : conditions) {
            if (!condition.isEmpty()) {
                condition.append(" && ");
            }
            condition.append("( ").append(cond).append(" )");
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
        } else {
            Matcher matcherWithoutReturns = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)").matcher(expression);
            if (matcherWithoutReturns.find()) {
                return matcherWithoutReturns.group(1);
            }
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

    public Map<String, List<String>> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, List<String>> specs) {
        this.specs = specs;
    }

    public String getMethodSignature() {
        return methodSignature;
    }
}
