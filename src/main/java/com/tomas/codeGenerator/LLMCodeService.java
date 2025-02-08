package com.tomas.codeGenerator;

import com.tomas.model.Message;
import com.tomas.model.Request;
import com.tomas.model.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LLMCodeService {

    private static final String url = "https://api.together.xyz/v1/chat/completions";
    private static final String apiKey = "8c61b03e1c9750e780cbc123679523d551be0171c65f32458966d831f6503552";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String codeRequest = "Generate code (and only the code) to the natural language problem presented below:\n";

    public String getCodeResponse(String userMessage) {
        String fullMessage = codeRequest + userMessage;
        Request request = new Request("meta-llama/Llama-3.3-70B-Instruct-Turbo-Free", List.of(new Message("user", codeRequest + fullMessage)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Request> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);

        if (response.getBody() == null || response.getBody().getChoices().isEmpty()) {
            return "I'm sorry, I don't understand.";
        } else {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        }
    }

}
