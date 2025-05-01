package com.tomas.evaluation;

public class EvaluationResult {

    int taskId;
    boolean specsEval;
    boolean codeEval;
    double consistencyEval;

    public EvaluationResult(int taskId, boolean specsEval, boolean codeEval, double consistencyEval) {
        this.taskId = taskId;
        this.specsEval = specsEval;
        this.codeEval = codeEval;
        this.consistencyEval = consistencyEval;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isSpecsEval() {
        return specsEval;
    }

    public void setSpecsEval(boolean specsEval) {
        this.specsEval = specsEval;
    }

    public double getConsistencyEval() {
        return consistencyEval;
    }

    public void setConsistencyEval(double consistencyEval) {
        this.consistencyEval = consistencyEval;
    }

    public boolean isCodeEval() {
        return codeEval;
    }

    public void setCodeEval(boolean codeEval) {
        this.codeEval = codeEval;
    }
}
