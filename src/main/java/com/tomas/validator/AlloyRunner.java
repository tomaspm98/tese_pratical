package com.tomas.validator;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import kodkod.engine.satlab.SATFactory;

import java.util.List;


public class AlloyRunner {

    public static void runAlloyModel(String alloyModel) {
        try {
            A4Reporter rep = new A4Reporter();
            CompModule world = CompUtil.parseEverything_fromString(rep, alloyModel);
            A4Options options = new A4Options();
            options.solver = SATFactory.get("sat4j");
            Command cmd = world.getAllCommands().get(0);
            A4Solution solution = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd, options);

            if (solution.satisfiable()) {
                System.out.println("SATISFIABLE: Solution found!");
                System.out.println(solution);
            } else {
                System.out.println("UNSATISFIABLE: No solution exists.");

            }
        } catch (Err e) {
            System.err.println("Alloy error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String model = """
            module Add

            sig Input {
                a: Int,
                b: Int
            }

            sig Output {
                result: Int
            }


            run {} for 50
        """;

        runAlloyModel(model);
    }
}

