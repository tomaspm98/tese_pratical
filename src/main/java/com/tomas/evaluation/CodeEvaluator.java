package com.tomas.evaluation;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeEvaluator {

    public boolean evaluateCode(String codeCleaned, List<String> codeTaskTestList) throws IOException, InterruptedException {
        String methodName = getMethodName(codeCleaned);

        File tempFile = new File("src/main/java/com/tomas/evaluation/tempFile.py");
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(codeCleaned + "\n");
        for (String assertion : codeTaskTestList) {
            assertion = assertion.replaceAll("(?<=assert\\s)(\\w+)(?=\\()", methodName);
            fileWriter.write(assertion + "\n");
        }
        fileWriter.close();

        ProcessBuilder processBuilder = new ProcessBuilder("python", tempFile.getPath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        Files.delete(tempFile.toPath());

        if (exitCode == 0) {
            System.out.println("All assertions passed!");
            return true;
        } else {
            System.err.println("Assertion failed:\n" + output);
            return false;
        }
    }

    public String getMethodName(String code) {
        Pattern pattern = Pattern.compile("(?<=def\\s)(\\w+)(?=\\()");
        Matcher matcher = pattern.matcher(code);

        String methodName = "";
        if (matcher.find()) {
            methodName = matcher.group(1);
        }

        return methodName;
    }
}
