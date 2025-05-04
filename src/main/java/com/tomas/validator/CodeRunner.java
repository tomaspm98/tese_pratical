package com.tomas.validator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeRunner {

    public String getOutputFromCode(Map<String, Object> inputsFromAlloy) throws IOException {
        Process process = getProcess(inputsFromAlloy);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                return line;
            }
        }
        return null;
    }

    private Process getProcess(Map<String, Object> inputsFromAlloy) throws IOException {
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add("src/main/resources/pythonCode.py");
        for (Map.Entry<String, Object> entry : inputsFromAlloy.entrySet()) {
            if (entry.getValue() instanceof ArrayList<?>) {
                String value = entry.getValue().toString();
                value = value.replace("[", "").replace("]", "");
                command.add(value);
            } else {
                command.add(entry.getValue().toString());
            }
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        return process;
    }
}
