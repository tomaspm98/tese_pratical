package com.tomas.input;

import com.tomas.validator.AlloyRunner;
import com.tomas.validator.FinalValidation;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/input")
@CrossOrigin(origins = "http://localhost:3000")
public class UserInput {

    private final AlloyRunner alloyRunner;
    private final FinalValidation finalValidation;

    public UserInput(AlloyRunner alloyRunner, FinalValidation finalValidation) {
        this.alloyRunner = alloyRunner;
        this.finalValidation = finalValidation;
    }

    @PostMapping
    public double userInput(@RequestBody String message) throws IOException, ScriptException {
        Set<Map<String,Integer>> inputsFromAlloy = alloyRunner.runAlloyModel(message);
        return finalValidation.conditionParser(inputsFromAlloy, message);
    }

}
