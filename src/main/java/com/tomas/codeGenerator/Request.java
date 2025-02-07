package com.tomas.codeGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class Request {
    private String model;
    private List<Message> messages;

    public Request(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }
}
