package com.tomas.validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeRunner {
    private final File code;

    public CodeRunner(File code) {
        this.code = code;
    }

    public int getOutputFromCode(List<Map<String, Integer>> inputsFromAlloy) throws IOException {
        int result = Integer.MIN_VALUE;
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(code.getPath());
        for (Map<String, Integer> input : inputsFromAlloy) {
            for (Map.Entry<String, Integer> entry : input.entrySet()) {
                command.add(entry.getValue().toString());
            }
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result = Integer.parseInt(line);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        CodeRunner codeRunner = new CodeRunner(new File("src/main/java/com/tomas/validator/CodeRunner.py"));
        List<Map<String, Integer>> inputsFromAlloy = new ArrayList<>();
        Map<String, Integer> inputMap = Map.of("a", 1, "b", 2);
        inputsFromAlloy.add(inputMap);
        System.out.println(inputsFromAlloy);
        int result = codeRunner.getOutputFromCode(inputsFromAlloy);
        System.out.println(result);
    }
}
