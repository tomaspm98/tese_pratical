package com.tomas.model;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
