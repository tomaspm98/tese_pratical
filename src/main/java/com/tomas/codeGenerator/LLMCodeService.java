package com.tomas.codeGenerator;

import com.tomas.model.Request;
import com.tomas.model.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class LLMCodeService {

    private static final String url = "http://localhost:11434/api/generate";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String codeRequest = "You are an expert in the Python programming language. Generate code in Python (and only code without comments and examples) to the natural language problem presented below, " +
                        "including just the auxiliar function to solve the problem and the main function (defined with def main():, " +
                        "that just calls the auxiliar function and prints its return value (don't include any additional logic neither output formatting to it). If the return value of the method is a pair, return it in the form '(pair[0], pair[1])'; otherwise just return the output value) " +
                        "that receives the inputs to run the method from sys, and don't use any natural language on the response. " +
                        "Also, always write the script so that it always uses command-line arguments (i.e sys.argv) to get input and don't use input(). Assume that lists in the sys.argv are given in this way \"1,2,3\", where the members of the list are separated by commas"+
                        "\n\nNOW, THIS IS YOUR PYTHON TASK TO SOLVE IN UPPERCASE: ";
    private static final String llmModel = "deepseek-coder-v2:16b";

    public String getCodeResponse(String userMessage) throws IOException {
        String fullMessage = codeRequest + userMessage;
        Request request = new Request(llmModel, codeRequest + fullMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
        code = code.replaceAll("^\\s*```python\\n?", "");
        code = code.replaceAll("\\n?```$", "");
        filewriter.write(code);
        filewriter.close();
    }

}
