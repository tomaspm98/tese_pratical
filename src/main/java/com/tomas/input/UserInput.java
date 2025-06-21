package com.tomas.input;

import com.tomas.model.InputResponse;
import com.tomas.util.NullOutputException;
import com.tomas.validator.AlloyRunner;
import com.tomas.validator.FinalValidation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/input")
@CrossOrigin(origins = "http://localhost:3000")
public class UserInput {

    private final AlloyRunner alloyRunner;
    private final FinalValidation finalValidation;
    private final RestTemplate restTemplate = new RestTemplate();
    private int retryCount = 0;

    public UserInput(AlloyRunner alloyRunner, FinalValidation finalValidation) {
        this.alloyRunner = alloyRunner;
        this.finalValidation = finalValidation;
    }

    @PostMapping
    public InputResponse userInput(@RequestBody List<String> message) throws IOException {
        String specs = restTemplate.postForObject("http://localhost:8080/specs-generator", message.getFirst(), String.class);
        double result;
        try {
            Set<Map<String,Object>> inputsFromAlloy = alloyRunner.runAlloyModel(specs);
            if (inputsFromAlloy.isEmpty()) {
                return new InputResponse(-2.0, "Error: Input type not supported by the system", specs);
            }
            result = finalValidation.conditionParser(inputsFromAlloy, message.get(1));
        } catch (NullOutputException | NullPointerException | IndexOutOfBoundsException e) {
            System.out.println("Retrying full flow due to: " + e.getMessage());
            retryCount++;
            if (retryCount > 3) {
                retryCount = 0;
                return new InputResponse(-1.0, "Error: Max retries reached (error building alloy model)", specs);
            }
            return userInput(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String code = Files.readString(Path.of("src/main/resources/pythonCode.py"));
        return new InputResponse(result, code, specs);
    }
}
