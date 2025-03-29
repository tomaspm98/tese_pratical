package com.tomas.input;

import com.tomas.model.InputResponse;
import com.tomas.validator.AlloyRunner;
import com.tomas.validator.FinalValidation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/input")
@CrossOrigin(origins = "http://localhost:3000")
public class UserInput {

    private final AlloyRunner alloyRunner;
    private final FinalValidation finalValidation;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserInput(AlloyRunner alloyRunner, FinalValidation finalValidation) {
        this.alloyRunner = alloyRunner;
        this.finalValidation = finalValidation;
    }

    @PostMapping
    public InputResponse userInput(@RequestBody String message) throws IOException, ScriptException {
        String specs = restTemplate.postForObject("http://localhost:8080/specs-generator", message, String.class);
        Set<Map<String,Integer>> inputsFromAlloy = alloyRunner.runAlloyModel(specs);
        Double result = finalValidation.conditionParser(inputsFromAlloy, message);
        String code = Files.readString(Path.of("src/main/resources/pythonCode.py"));
        return new InputResponse(result, code, specs);
    }

}
