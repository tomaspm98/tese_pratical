package com.tomas.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            preconditions.add(requiresMatcher.group(1).trim());
        }

        while (ensuresMatcher.find()) {
            postconditions.add(ensuresMatcher.group(1).trim());
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

    public Map<String, String> getSpecs() {
        return specs;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

}
