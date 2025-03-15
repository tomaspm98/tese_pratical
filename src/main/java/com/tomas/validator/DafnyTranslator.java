package com.tomas.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyTranslator {

    private Map<String, String> specs = new HashMap<>();

    public Map<String, String> extractSpecs(String code) {

        specs = new HashMap<>();

        Matcher requiresMatcher = Pattern.compile("requires (.+)").matcher(code);
        Matcher ensuresMatcher = Pattern.compile("ensures (.+)").matcher(code);

        if (requiresMatcher.find()) {
            specs.put("precondition", requiresMatcher.group(1).trim());
        }
        if (ensuresMatcher.find()) {
            specs.put("postcondition", ensuresMatcher.group(1).trim());
        }
        return specs;
    }

    public String extractMethodSignature (String code) {
        Pattern methodPattern = Pattern.compile("(method\\s+.+)");
        Matcher methodMatcher = methodPattern.matcher(code);
        if (methodMatcher.find()) {
            return methodMatcher.group(1).trim();
        }
        return null;
    }

    public Map<String, String> getSpecs() {
        return specs;
    }

}
