package com.tomas.model;

public class Response {
    private String response;
    private boolean done;

    public Response(String response, boolean done) {
        this.response = response;
        this.done = done;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
