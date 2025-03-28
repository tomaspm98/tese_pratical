package com.tomas.evaluation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<CodeTask> readJsonlFile(String filePath) {
        List<CodeTask> codeTasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                CodeTask task = objectMapper.readValue(line, CodeTask.class);
                codeTasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codeTasks;
    }
}
