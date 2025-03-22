package com.tomas.specificationsGenerator;

import com.tomas.validator.DafnyToAlloyConverter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specs-generator")
public class SpecsGeneratorController {

    private final LLMSpecsService llmSpecsService;
    private final DafnyToAlloyConverter dafnyToAlloyConverter;

    public SpecsGeneratorController(LLMSpecsService llmSpecsService, DafnyToAlloyConverter dafnyToAlloyConverter) {
        this.llmSpecsService = llmSpecsService;
        this.dafnyToAlloyConverter = dafnyToAlloyConverter;
    }

    @PostMapping
    public String generateSpecs(@RequestBody String message) {
        return llmSpecsService.getSpecsResponse(message);
    }
}
