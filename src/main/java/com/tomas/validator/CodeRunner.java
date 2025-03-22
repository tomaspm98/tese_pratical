package com.tomas.validator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeRunner {

    public int getOutputFromCode(Map<String, Integer> inputsFromAlloy) throws IOException {
        int result = Integer.MIN_VALUE;
        Process process = getProcess(inputsFromAlloy);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result = Integer.parseInt(line);
            }
        }
        return result;
    }

    private Process getProcess(Map<String, Integer> inputsFromAlloy) throws IOException {
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add("src/main/resources/pythonCode.py");
        for (Map.Entry<String, Integer> entry : inputsFromAlloy.entrySet()) {
            command.add(entry.getValue().toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        return process;
    }
}
