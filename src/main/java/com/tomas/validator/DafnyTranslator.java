package com.tomas.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DafnyTranslator {

    public Map<String, String> extractSpecs(String code) {

        Map<String, String> specs = new HashMap<>();

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

    public static void main(String[] args) {
        DafnyTranslator dafnyTranslator = new DafnyTranslator();
        String code = "method sum(a: int, b: int) returns (c: int)\n" +
                "requires a >= 0 && b >= 0\n" +
                "ensures c == a + b\n" +
                "{\n" +
                "c := a + b;\n" +
                "}";
        System.out.println(dafnyTranslator.extractSpecs(code));
        System.out.println(dafnyTranslator.extractMethodSignature(code));
    }
}
