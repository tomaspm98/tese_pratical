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
                preconditions.add(precondition);
            }

            while (ensuresMatcher.find()) {
                String postcondition = ensuresMatcher.group(1);
                postcondition = postcondition.replaceAll(";", "");
                postcondition = postcondition.replaceAll("//.*", "");
                postconditions.add(postcondition);
            }
            specs.put("precondition", constructOneCondition(preconditions));
            specs.put("postcondition", constructOneCondition(postconditions));
        }

        return specs;
    }

    private String normalizeComparation(String expression) {
        String[] tokens = expression.split("(?<=[<>]=?|>)|(?=[<>]=?|<>)");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        if (tokens.length == 5) {
            String left = tokens[0] + " " + tokens[1] + " " + tokens[2];
            String right = tokens[2] + " " + tokens[3] + " " + tokens[4];
            result.append(left).append(" && ").append(right);
        } else {
            result.append(expression);
        }

        return result.toString();
    }

    private String constructOneCondition(List<String> conditions) {
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
                method UniqueProduct (arr: array<int>) returns (product: int)
                   ensures product == SetProduct((set i | 0 <= i < arr.Length :: arr[i]))
                {
                    var p := 1;
                    var seen: set<int> := {};
                   \s
                    for i := 0 to arr.Length
                        invariant 0 <= i <= arr.Length
                        invariant seen == (set k | 0 <= k < i :: arr[k])
                        invariant p == SetProduct((set k | 0 <= k < i :: arr[k]))
                    {
                        if ! (arr[i] in seen) {
                            seen := seen + { arr[i] };
                            p := p * arr[i];
                            SetProductLemma(seen, arr[i]);
                        }
                    }
                    product := p;
                }
                
                ghost function SetProduct(s : set<int>) : int
                {
                    if s == {} then 1
                    else var x :| x in s;\s
                         x * SetProduct(s - {x})
                }
                
                /*\s
                 * This is necessary because, when we add one element, we need to make sure
                 * that the product of the new set, as a whole, is the same as the product
                 * of the old set times the new element.
                 */
                lemma SetProductLemma(s: set <int>, x: int)\s
                 requires x in s
                 ensures SetProduct(s - {x}) * x == SetProduct(s)
                {
                   if s != {}
                   {
                      var y :| y in s && y * SetProduct(s - {y}) == SetProduct(s);
                      if y == x {}
                      else {
                         calc {
                            SetProduct(s);
                            y * SetProduct(s - {y});
                            { SetProductLemma(s - {y}, x); }
                            y * x * SetProduct(s - {y} - {x});
                            { assert s - {x} - {y} == s - {y} - {x};}
                            y * x * SetProduct(s - {x} - {y});
                            x * y * SetProduct(s - {x} - {y});
                            { SetProductLemma(s - {x}, y); }
                            x * SetProduct(s - {x});
                         }
                      }
                   }
                }
                """;

        DafnyTranslator translator = new DafnyTranslator();
        String specs = translator.extractVariables(dafnyCode);
        String methodSignature = translator.extractMethodSignature(dafnyCode);
        Map<String, String> dafnySpecs = translator.extractSpecs(dafnyCode);

        System.out.println("Extracted Variables: " + dafnySpecs);
        System.out.println("Specs: " + specs);
        System.out.println("Method Signature: " + methodSignature);
    }

}
