package com.tomas.evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvaluationResultWriter {

    private List<EvaluationResult> results = new ArrayList<>();

    public void addResult(int taskId, boolean specsEval, boolean codeEval, double consistencyEval, String repoDafny, String myRepoCode) {
        results.add(new EvaluationResult(taskId, specsEval, codeEval, consistencyEval, repoDafny, myRepoCode));
    }

    public void writeCsv() {
        try (FileWriter writer = new FileWriter("src/main/java/com/tomas/evaluation/results.csv")) {
            writer.write("task_id,specs_eval,code_eval,consistency_eval,code,specs,dafny_repo_pointer,my_repo_code_pointer\n");
            for (EvaluationResult result : results) {
                writer.write(
                            result.taskId + "," +
                                result.specsEval + "," +
                                result.codeEval + "," +
                                result.consistencyEval + "," +
                                result.repoDafny + "," +
                                result.myRepoCode + "\n"
                );
            }
            System.out.println("CSV file written successfully.");
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }

}
