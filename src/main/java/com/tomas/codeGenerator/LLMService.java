package com.tomas.codeGenerator;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class LLMService {

    private String url = "https://api.together.xyz/v1/chat/completions";
    private String apiKey = "8c61b03e1c9750e780cbc123679523d551be0171c65f32458966d831f6503552";
    private RestTemplate restTemplate = new RestTemplate();

    public String getChatGptResponse(String userMessage) {
        // Create the request payload
        Request request = new Request("meta-llama/Llama-3.3-70B-Instruct-Turbo-Free", List.of(new Message("user", userMessage)));

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Create request entity
        HttpEntity<Request> entity = new HttpEntity<>(request, headers);

        // Make API call
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
        // Extract response
        return response.getBody().getChoices().get(0).getMessage().getContent();
    }

}
