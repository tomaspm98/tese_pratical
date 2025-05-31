package com.tomas.evaluation;

import com.tomas.util.Util;
import com.tomas.validator.DafnyTranslator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpecsEvaluator {

    DafnyTranslator dafnyTranslator = new DafnyTranslator();

    public boolean evaluateSpecs(String specs, int task_id) throws IOException, InterruptedException {
        Map<String, List<String>> specsConditions = dafnyTranslator.extractSpecs(specs);
        Map<String, String> specsConditionsIndividual = new HashMap<>();
        Map<String, String> specsConditionsFileIndividual = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : specsConditions.entrySet()) {
            String individualCondition = dafnyTranslator.constructOneCondition(entry.getValue());
            specsConditionsIndividual.put(entry.getKey(), individualCondition);
        }
        if (!specsConditionsIndividual.containsKey("precondition")) {
            specsConditionsIndividual.put("precondition", "false");
            System.out.println("Failed to get precondition!");
        }
        if (!specsConditionsIndividual.containsKey("postcondition")) {
            specsConditionsIndividual.put("postcondition", "false");
            System.out.println("Failed to get postcondition!");
        }
        if (specsConditionsIndividual.get("precondition").isEmpty()) {
            specsConditionsIndividual.put("precondition", "true");
        }
        if (specsConditionsIndividual.get("postcondition").isEmpty()) {
            specsConditionsIndividual.put("postcondition", "true");
        }
        String methodSignature = dafnyTranslator.extractMethodSignature(specs);
        List<String> params = new ArrayList<>();
        List<String> returns = new ArrayList<>();
        String variables = "";

        Matcher matcher = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignature);
        if (matcher.find()) {
            String paramsRaw = matcher.group(1);
            String returnsRaw = matcher.group(2);

            params = Util.extractVariableNames(paramsRaw);
            returns = Util.extractVariableNames(returnsRaw);
            variables = paramsRaw + ", " + returnsRaw;
        } else {
            Matcher matcherWithoutReturns = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)").matcher(methodSignature);
            if (matcherWithoutReturns.find()) {
                String paramsRaw = matcherWithoutReturns.group(1);
                params = Util.extractVariableNames(paramsRaw);
                variables = paramsRaw;
            }
        }

        String taskIdFile = "task_id_" + task_id + ".dfy";
        String codeInFile = Files.readString(Path.of("src/main/java/com/tomas/evaluation/dafny/" + taskIdFile));

        Map<String, List<String>> specsConditionsFile = dafnyTranslator.extractSpecs(codeInFile);
        for (Map.Entry<String, List<String>> entry : specsConditionsFile.entrySet()) {
            String individualCondition = dafnyTranslator.constructOneCondition(entry.getValue());
            specsConditionsFileIndividual.put(entry.getKey(), individualCondition);
        }
        String methodSignatureFile = dafnyTranslator.extractMethodSignature(codeInFile);

        Matcher matcherFile = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)\\s+returns\\s*\\(([^)]*)\\)").matcher(methodSignatureFile);
        if (matcherFile.find()) {
            String paramsRaw = matcherFile.group(1);
            String returnsRaw = matcherFile.group(2);

            List<String> paramsFile = Util.extractVariableNames(paramsRaw);
            List<String> returnsFile = Util.extractVariableNames(returnsRaw);

            for (int i = 0; i < paramsFile.size(); i++) {
                if (specsConditionsFileIndividual.get("precondition") == null || specsConditionsFileIndividual.get("precondition").isEmpty()) {
                    specsConditionsFileIndividual.put("precondition", "true");
                    break;
                }
                String newPrecondition = specsConditionsFileIndividual.get("precondition").replaceAll(paramsFile.get(i), params.get(i));
                String newPreconditionOriginal = specsConditionsIndividual.get("precondition").replaceAll(";", "");
                specsConditionsFileIndividual.put("precondition", newPrecondition);
                specsConditionsIndividual.put("precondition", newPreconditionOriginal);
                String newPostcondition = specsConditionsFileIndividual.get("postcondition").replaceAll("(?<!r)" + Pattern.quote(paramsFile.get(i)) + "(?!l)", params.get(i));
                specsConditionsFileIndividual.put("postcondition", newPostcondition);
            }
            for (int i = 0; i < returnsFile.size(); i++) {
                if (specsConditionsFileIndividual.get("postcondition") == null || specsConditionsFileIndividual.get("postcondition").isEmpty()) {
                    specsConditionsFileIndividual.put("postcondition", "true");
                    break;
                }
                String newPostcondition = specsConditionsFileIndividual.get("postcondition").replaceAll("(?<!r)" + Pattern.quote(returnsFile.get(i)) + "(?!l)", returns.get(i));
                newPostcondition = newPostcondition.replaceAll( ";", "");
                specsConditionsFileIndividual.put("postcondition", newPostcondition);
                String newPostconditionOriginal = specsConditionsIndividual.get("postcondition").replaceAll(";", "");
                specsConditionsIndividual.put("postcondition", newPostconditionOriginal);
            }
        } else {
            Matcher matcherWithoutReturnsFile = Pattern.compile("method\\s+\\w+\\s*\\(([^)]*)\\)").matcher(methodSignatureFile);
            if (matcherWithoutReturnsFile.find()) {
                String paramsRaw = matcherWithoutReturnsFile.group(1);

                List<String> paramsFile = Util.extractVariableNames(paramsRaw);

                for (int i = 0; i < paramsFile.size(); i++) {
                    if (specsConditionsFileIndividual.get("precondition") == null || specsConditionsFileIndividual.get("precondition").isEmpty()) {
                        specsConditionsFileIndividual.put("precondition", "true");
                        break;
                    }
                    String newPrecondition = specsConditionsFileIndividual.get("precondition").replaceAll(paramsFile.get(i), params.get(i));
                    String newPreconditionOriginal = specsConditionsIndividual.get("precondition").replaceAll(";", "");
                    specsConditionsFileIndividual.put("precondition", newPrecondition);
                    specsConditionsIndividual.put("precondition", newPreconditionOriginal);
                    String newPostcondition = specsConditionsFileIndividual.get("postcondition").replaceAll("(?<!r)" + Pattern.quote(paramsFile.get(i)) + "(?!l)", params.get(i));
                    specsConditionsFileIndividual.put("postcondition", newPostcondition);
                }
            }
        }

        String auxiliarFunctions = extractAuxiliarFunctions(codeInFile);

        return equivalentConditions(variables, specsConditionsIndividual.get("precondition"), specsConditionsFileIndividual.get("precondition"), auxiliarFunctions) && equivalentConditions(variables, specsConditionsIndividual.get("postcondition"), specsConditionsFileIndividual.get("postcondition"), auxiliarFunctions);
    }

    public boolean equivalentConditions(String variables, String conditionGenerated, String conditionFile, String auxiliarFunctions) throws IOException, InterruptedException {
        String code = generate(variables, conditionGenerated, conditionFile, auxiliarFunctions);
        Path dafnyFile = write(code);

        return verify(dafnyFile);
    }

    public String generate(String variables, String cond1, String cond2, String auxiliarFunctions) {
        String paramNames = extractParamNames(variables);
        //Para adicionar reads quando as funções usam arrays
        List<String> readsStatements = new ArrayList<>();
        Matcher arrayMatcher = Pattern.compile("\\b(\\w+)\\s*:\\s*array<[^>]+>").matcher(variables);
        while (arrayMatcher.find()) {
            String nameVar = arrayMatcher.group(1);
            readsStatements.add("reads " + nameVar);
        }
        String finalReads = "";
        if (!readsStatements.isEmpty()) {
            finalReads = String.join("\n ", readsStatements);
        }

        return """
            lemma CheckEquivalence()
              ensures forall %s :: Cond1(%s) <==> Cond2(%s)
            {
              forall %s
                ensures Cond1(%s) <==> Cond2(%s)
              {
                if Cond1(%s)
                {
                  assert Cond2(%s);
                }
                if Cond2(%s)
                {
                  assert Cond1(%s);
                }
              }
            }

            function Cond1(%s): bool
            %s
            {
              %s
            }

            function Cond2(%s): bool
            %s
            {
              %s
            }
           \s
            %s
           \s"""
                .formatted(
                        variables, paramNames, paramNames,
                        variables, paramNames, paramNames,
                        paramNames, paramNames, paramNames, paramNames,
                        variables, finalReads, cond1,
                        variables, finalReads, cond2,
                        auxiliarFunctions
                );
    }

    private String extractParamNames(String declaration) {
        return Arrays.stream(declaration.split(","))
                .map(String::trim)
                .map(s -> s.split(":")[0].trim())
                .collect(Collectors.joining(", "));
    }

    public Path write(String content) throws IOException {
        Path file = Files.createTempFile("equivalencyBetweenConditions", ".dfy");
        Files.writeString(file, content);
        return file;
    }

    public boolean verify(Path dafnyFile) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "dotnet",
                "C:\\Users\\Tomas Maciel\\.vscode\\extensions\\dafny-lang.ide-vscode-3.4.4\\out\\resources\\4.10.0\\github\\dafny\\Dafny.dll",
                "verify",
                dafnyFile.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output = new String(process.getInputStream().readAllBytes());
        process.waitFor();

        return output.contains("verified, 0 errors");
    }

    public String extractAuxiliarFunctions(String codeInFile) throws IOException {
        StringBuilder result = new StringBuilder();

        Pattern functionPattern = Pattern.compile(
                "(?m)(ghost\\s+)?(function|predicate)\\s+[^\\n]*\\n(?:\\s+[^\\n]*\\n)*?\\{(?:[^{}]*|\\{[^{}]*})*}"
        );

        Matcher matcher = functionPattern.matcher(codeInFile);

        while (matcher.find()) {
            String functionBlock = matcher.group();
            functionBlock = functionBlock.replaceFirst("\\bghost\\s+", "");
            result.append(functionBlock).append("\n\n");
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        String code = """
                predicate IsDigit(c: char)
                {
                    48 <= c as int <= 57
                }
                
                
                method CountDigits(s: string) returns (count: int)
                    ensures count >= 0
                    ensures count == | set i: int | 0 <= i < |s| && IsDigit(s[i])|
                {
                    var digits := set i: int | 0 <= i < |s| && IsDigit(s[i]);
                    count := |digits|;
                }
                
                
                """;

        SpecsEvaluator specsEvaluator = new SpecsEvaluator();
        String ok = specsEvaluator.generate(
                "s: array<string>, count: array<int>",
                "count >= 0 && count == | set i: int | 0 <= i < |s| && IsDigit(s[i])",
                "count >= 0 && count == | set i: int | 0 <= i < |s| && IsDigit(s[i])",
                specsEvaluator.extractAuxiliarFunctions(code)
        );
        System.out.println(ok);
    }
}
