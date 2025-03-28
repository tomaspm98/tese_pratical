package com.tomas.evaluation;

import com.tomas.model.Request;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Evaluation {

    public void evaluateIndividual(CodeTask codeTask) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/input";

        String message = codeTask.getText();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.POST, entity, Double.class);

        //extrair código + specs

    }

    public static void main(String[] args) {
        JsonReader jsonReader = new JsonReader();
        List<CodeTask> listCode = jsonReader.readJsonlFile("src/main/java/com/tomas/evaluation/mbpp.jsonl");
        Evaluation evaluation = new Evaluation();
        evaluation.evaluateIndividual(listCode.getFirst());
    }
}
