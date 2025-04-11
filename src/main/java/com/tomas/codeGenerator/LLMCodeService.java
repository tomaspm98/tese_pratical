package com.tomas.codeGenerator;

import com.tomas.model.Message;
import com.tomas.model.Request;
import com.tomas.model.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class LLMCodeService {

    private static final String url = "http://localhost:11434/api/generate";
    //private static final String apiKey = "8c61b03e1c9750e780cbc123679523d551be0171c65f32458966d831f6503552";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String codeRequest = "Generate code in Python (and only code without comments and examples) to the natural language problem presented below, including just the auxiliar function to solve the problem and the main function (defined with def main():, that just calls the auxiliar function and prints its return value (don't include any additional logic neither output formatting to it)) that receives the inputs to run the method from sys, and don't use any natural language on the response:\n";
    private static final String llmModel = "deepseek-coder-v2:16b";

    public String getCodeResponse(String userMessage) throws IOException {
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
            writeToFile(response.getBody().getResponse());
            return response.getBody().getResponse();
        }
    }

    public void writeToFile(String code) throws IOException {
        File file = new File("src/main/resources/pythonCode.py");
        FileWriter filewriter = new FileWriter(file);
        code = code.replaceAll("^\\s*```python\\n?", ""); // Removes ```python at the start
        code = code.replaceAll("\\n?```$", "");
        filewriter.write(code);
        filewriter.close();
    }

}
