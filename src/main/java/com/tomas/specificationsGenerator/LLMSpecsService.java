package com.tomas.specificationsGenerator;

import com.tomas.model.Message;
import com.tomas.model.Request;
import com.tomas.model.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LLMSpecsService {

    private static final String url = "http://localhost:11434/api/generate";
    //private static final String apiKey = "8c61b03e1c9750e780cbc123679523d551be0171c65f32458966d831f6503552";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String codeRequest = "Generate specifications in Dafny (only the code in Dafny language, the language is presented in https://dafny.org/), don't produce any natural language or code comments, only the code I ask you containing pre-conditions (represented with requires), post-conditions (represented with ensures) and the logic code to the natural language problem presented below, without using int.minValue and int.MaxValue that aren't valid and only containing valid alloy syntax on Dafny, and write only methods \n";
    private static final String llmModel = "deepseek-coder-v2:16b";

    public String getSpecsResponse(String userMessage) {
        String fullMessage = codeRequest + userMessage;
        Request request = new Request(llmModel, codeRequest + fullMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setBearerAuth(apiKey);

        HttpEntity<Request> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);

        if (response.getBody() == null || response.getBody().getResponse() == null) {
            return "I'm sorry, I don't understand.";
        } else {
            return response.getBody().getResponse();
        }
    }

}
