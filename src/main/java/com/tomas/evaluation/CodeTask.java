package com.tomas.evaluation;

import java.util.List;

public class CodeTask {
    
    private String text;
    
    private String code;
    
    private int task_id;

    private String test_setup_code;

    private List<String> test_list;

    private List<String> challenge_test_list;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTest_setup_code() {
        return test_setup_code;
    }

    public void setTest_setup_code(String test_setup_code) {
        this.test_setup_code = test_setup_code;
    }

    public List<String> getChallenge_test_list() {
        return challenge_test_list;
    }

    public void setChallenge_test_list(List<String> challengetest_list) {
        this.challenge_test_list = challengetest_list;
    }

    public List<String> getTest_list() {
        return test_list;
    }

    public void setTest_list(List<String> test_list) {
        this.test_list = test_list;
    }
}

