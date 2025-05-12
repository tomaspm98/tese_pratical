package com.tomas.specificationsGenerator;

import com.tomas.model.Request;
import com.tomas.model.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LLMSpecsService {

    private static final String url = "http://localhost:11434/api/generate";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String codeRequest = "Generate specifications in Dafny (only the code in Dafny language, the language is presented in https://dafny.org/), " +
                        "don't produce any natural language or code comments, only the code I ask you containing pre-conditions " +
                        "(only when strictly necessary and essential for the program logic, represented with requires, all in 1 line), " +
                        "post-conditions (only when necessary, represented with ensures, all in 1 line, under the pre-conditions) " +
                        "and the logic code to the natural language problem presented below, without using int.minValue and int.MaxValue that aren't valid " +
                        "and only containing valid alloy syntax on Dafny, and write only methods whose the only signature accepted is " +
                        "method <method_name>(inputVariables) returns (outputVariables), don't use -> instead of returns keyword, " +
                        "also don't use any built-in math functions or libraries like Math.PI, sqrt() and pow() " +
                        "and only rely on basic arithmetic operations (addition, subtraction, multiplication, division and exponentiation) " +
                        "and manually define constants when necessary (for example, instead of using Math.pi, use 3.141592653589793), " +
                        "and use array or seq to represent lists in Dafny. Here is an example of how the method in Dafny must be written:\n" +
                    """
                    method templateMethod(x: int) returns (y: int)
                          requires x >= 0
                          ensures y >= x
                        {
                          y := x + 1;
                        }
                    """;
    private static final String llmModel = "deepseek-coder-v2:16b";

    public String getSpecsResponse(String userMessage) {
        String fullMessage = codeRequest + userMessage;
        Request request = new Request(llmModel, codeRequest + fullMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Request> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);

        if (response.getBody() == null || response.getBody().getResponse() == null) {
            return "I'm sorry, I don't understand.";
        } else {
            return response.getBody().getResponse();
        }
    }

}
