package com.tomas.evaluation;

import com.tomas.model.InputResponse;
import com.tomas.validator.DafnyTranslator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;

public class Evaluation {

    CodeEvaluator codeEvaluator = new CodeEvaluator();
    SpecsEvaluator specsEvaluator = new SpecsEvaluator();
    private int totalCounter = 0;
    private int counterCorrectCode = 0;
    private int counterCorrectSpecs = 0;
    private int counterConsistencyDetections = 0;
    private boolean correctCode;
    private boolean correctSpecs;

    public void evaluateIndividual(CodeTask codeTask) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/input";

        String message = codeTask.getText() + "(integer problem)";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<InputResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, InputResponse.class);

        String codeCleaned = response.getBody().getCodeGenerated().replaceAll("(?s)def main\\(\\):.*", "");
        String specs = response.getBody().getSpecsGenerated();

        if (specsEvaluator.evaluateSpecs(specs, codeTask.getTask_id())) {
            System.out.println("Specs evaluation passed! Problem number" + (totalCounter+1));
            correctSpecs = true;
            counterCorrectSpecs++;
        } else {
            correctSpecs = false;
            System.err.println("Specs evaluation failed!");
        }

        if (codeEvaluator.evaluateCode(codeCleaned, codeTask.getTest_list())) {
            System.out.println("Code evaluation passed!");
            correctCode = true;
            counterCorrectCode++;
        } else {
            correctCode = false;
            System.err.println("Code evaluation failed!");
        }
        if (!correctCode && !correctSpecs) {
            System.out.println("Code and specs evaluation failed!");
        } else if (correctSpecs && correctCode && response.getBody().getResult() == 1.0) {
            System.out.println("Code and specs evaluation passed!");
            counterConsistencyDetections++;
            totalCounter++;
        } else if (correctSpecs && !correctCode && response.getBody().getResult() < 1.0) {
            System.out.println("Code evaluation failed but specs evaluation passed!");
            counterConsistencyDetections++;
            totalCounter++;
        } else if (!correctSpecs && correctCode && response.getBody().getResult() < 1.0) {
            System.out.println("Code evaluation passed but specs evaluation failed!");
            counterConsistencyDetections++;
            totalCounter++;
        } else {
            System.out.println("Consistency wrongly detected!");
            totalCounter++;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Evaluation evaluation = new Evaluation();
        JsonReader jsonReader = new JsonReader();
        List<CodeTask> listCode = jsonReader.readJsonlFile("src/main/java/com/tomas/evaluation/mbpp.jsonl");
        for (CodeTask codeTask : listCode) {
            evaluation.evaluateIndividual(codeTask);
        }
        System.out.println("Consistency detections: " + evaluation.counterConsistencyDetections);
        System.out.println("Total evaluations: " + evaluation.totalCounter);
        System.out.println("Correct code evaluations: " + evaluation.counterCorrectCode);
        System.out.println("Correct specs evaluations: " + evaluation.counterCorrectSpecs);
    }
}

