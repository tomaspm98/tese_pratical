package com.tomas.model;

import lombok.Data;

import java.util.List;

@Data
public class Response {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
