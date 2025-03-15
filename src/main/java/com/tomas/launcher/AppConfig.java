package com.tomas.launcher;

import com.tomas.validator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DafnyTranslator dafnyTranslator() {
        return new DafnyTranslator();
    }

    @Bean
    public DafnyToAlloyConverter dafnyToAlloyConverter() {
        return new DafnyToAlloyConverter(dafnyTranslator());
    }

    @Bean
    public AlloyRunner alloyRunner() {
        return new AlloyRunner(dafnyToAlloyConverter(), restTemplate());
    }

    @Bean
    public CodeRunner codeRunner() {
        return new CodeRunner();
    }

    @Bean
    public FinalValidation finalValidation() {
        return new FinalValidation(codeRunner(), dafnyTranslator(), restTemplate());
    }
}

