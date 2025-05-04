package com.tomas.specificationsGenerator;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specs-generator")
public class SpecsGeneratorController {

    private final LLMSpecsService llmSpecsService;

    public SpecsGeneratorController(LLMSpecsService llmSpecsService) {
        this.llmSpecsService = llmSpecsService;
    }

    @PostMapping
    public String generateSpecs(@RequestBody String message) {
        return llmSpecsService.getSpecsResponse(message);
    }
}
