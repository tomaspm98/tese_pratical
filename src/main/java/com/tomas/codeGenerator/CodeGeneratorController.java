package com.tomas.codeGenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code-generator")
public class CodeGeneratorController {

    private final LLMCodeService llmCodeService;

    public CodeGeneratorController(LLMCodeService llmCodeService) {
        this.llmCodeService = llmCodeService;
    }

    @GetMapping
    public String generateCode(@RequestParam String message) {
        return llmCodeService.getCodeResponse(message);
    }
}
