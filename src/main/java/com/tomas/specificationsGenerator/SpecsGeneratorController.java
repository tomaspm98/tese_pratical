package com.tomas.specificationsGenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specs-generator")
public class SpecsGeneratorController {

    private final LLMSpecsService llmSpecsService;

    public SpecsGeneratorController(LLMSpecsService llmSpecsService) {
        this.llmSpecsService = llmSpecsService;
    }

    @GetMapping
    public String generateSpecs(@RequestParam String message) {
        return llmSpecsService.getSpecsResponse(message);
    }
}
