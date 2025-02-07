package com.tomas.codeGenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code-generator/chat")
public class CodeGeneratorController {

    private final LLMService llmService;

    public CodeGeneratorController(LLMService llmService) {
        this.llmService = llmService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return llmService.getChatGptResponse(message);
    }
}
