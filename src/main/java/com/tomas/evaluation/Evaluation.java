package com.tomas.evaluation;

import com.tomas.model.InputResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluation {

    public boolean evaluateIndividual(CodeTask codeTask) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/input";

        String message = codeTask.getText();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<InputResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, InputResponse.class);

        String codeCleaned = response.getBody().getCodeGenerated().replaceAll("(?s)def main\\(\\):.*", "");
        String methodName = getMethodName(codeCleaned);

        File tempFile = new File("src/main/java/com/tomas/evaluation/tempFile.py");
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(codeCleaned + "\n");
        for (String assertion : codeTask.getTest_list()) {
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
            methodName = matcher.group(1); // Capture group 1 contains the method name
        }

        return methodName;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        JsonReader jsonReader = new JsonReader();
        List<CodeTask> listCode = jsonReader.readJsonlFile("src/main/java/com/tomas/evaluation/mbpp.jsonl");
        Evaluation evaluation = new Evaluation();
        boolean eval = evaluation.evaluateIndividual(listCode.getFirst());
    }
}
