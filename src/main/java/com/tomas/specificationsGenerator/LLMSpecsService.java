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
    private static final String codeRequest = "You are an expert writing Dafny programs. You are very good at writing verifiable and accurate preconditions and postconditions. Generate specifications in Dafny (only the code in Dafny language without any natural language above it, the language is presented in https://dafny.org/), " +
                        "don't produce any natural language or code comments or explanations, only the code I ask you containing pre-conditions " +
                        "(only when strictly necessary and essential for the program logic otherwise don't produce any pre-condition, and don't include any pre-conditions for the integer range since int is already bounded by default, represented with requires), " +
                        "post-conditions (only when necessary, represented with ensures, under the pre-conditions) " +
                        "and the logic code to the natural language problem presented below, without using int.minValue and int.maxValue that aren't valid." +
                        "The preconditions (when necessary) and postconditions are always written under the method signature, and you always need to place a '{' under the last postcondition before writing the method's body." +
                        "Also don't use any built-in math functions or libraries like Math.PI, sqrt() and pow() " +
                        "and only rely on basic arithmetic operations (addition, subtraction, multiplication and division) and never use the Dafny type nat but int instead." +
                        "The length of an array s is represented by s.Length, while the length of a seq s is represented by |s|." +
                        "Manually define constants when necessary (for example, instead of using Math.pi, use 3.141592653589793) " +
                        "and use array or seq to represent lists in Dafny. Don't use Dafny's built-in tuples (e.g., (a,b)), but instead always represent tuples using arrays. For example, instead of writing (int,int), write array<int>. In Dafny, conjunction is represented by && and disjunction is represented by ||." +
            "Here is an example of how the method in Dafny must be written to be syntactically correct (it contains the method signature, preconditions (when necessary), postconditions and the method body always, and don't forget the parentheses and brackets when writing the code):\n\n" +
                    """
                    method templateMethod(x: int) returns (y: int)
                          requires x >= 0
                          ensures y >= x
                        {
                          y := x + 1;
                        }
                    """ + "\n\nNOW, THIS IS YOUR TASK IN DAFNY TO SOLVE IN UPPERCASE: ";
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
