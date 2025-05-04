package com.tomas.model;

public class InputResponse {

    private Double result;
    private String codeGenerated;
    private String specsGenerated;

    public InputResponse(Double result, String codeGenerated, String specsGenerated) {
        this.result = result;
        this.codeGenerated = codeGenerated;
        this.specsGenerated = specsGenerated;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getCodeGenerated() {
        return codeGenerated;
    }

    public void setCodeGenerated(String codeGenerated) {
        this.codeGenerated = codeGenerated;
    }

    public String getSpecsGenerated() {
        return specsGenerated;
    }

    public void setSpecsGenerated(String specsGenerated) {
        this.specsGenerated = specsGenerated;
    }
}
