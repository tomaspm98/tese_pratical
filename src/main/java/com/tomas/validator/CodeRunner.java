package com.tomas.validator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
            if (entry.getValue() instanceof ArrayList<?> list) {
                StringBuilder listSeparatedByCommas = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    listSeparatedByCommas.append(list.get(i).toString());
                    if (i < list.size() - 1) {
                        listSeparatedByCommas.append(", ");
                    }
                }
                command.add(listSeparatedByCommas.toString());
            } else {
                command.add(entry.getValue().toString());
            }
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        return process;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Object> inputs = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        inputs.put("a", list);
        CodeRunner codeRunner = new CodeRunner();
        codeRunner.getProcess(inputs);
    }
}
