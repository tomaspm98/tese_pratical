package com.tomas.codeGenerator;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/code-generator")
@CrossOrigin(origins = "http://localhost:3000")
public class CodeGeneratorController {

    private final LLMCodeService llmCodeService;

    public CodeGeneratorController(LLMCodeService llmCodeService) {
        this.llmCodeService = llmCodeService;
    }

    @GetMapping
    public String generateCode(@RequestParam String message) throws IOException {
        return llmCodeService.getCodeResponse(message);
    }
}
