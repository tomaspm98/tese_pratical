package com.tomas.validator;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprVar;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.*;
import kodkod.engine.satlab.SATFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlloyRunner {

    public static List<Map<String, Integer>> runAlloyModel(String alloyModel) {
        List<Map<String, Integer>> inputList = new ArrayList<>();

        try {
            A4Reporter rep = new A4Reporter();
            CompModule world = CompUtil.parseEverything_fromString(rep, alloyModel);
            A4Options options = new A4Options();
            options.solver = SATFactory.get("sat4j");

            Command cmd = world.getAllCommands().get(0);
            A4Solution solution = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd, options);

            if (solution.satisfiable()) {
                for (Sig sig : world.getAllReachableSigs()) {
                    if (sig.label.equals("this/Input")) {
                        A4TupleSet tuples = solution.eval(sig);

                        for (A4Tuple tuple : tuples) {
                            Map<String, Integer> inputMap = new HashMap<>();

                            for (Sig.Field field : sig.getFields()) {
                                A4TupleSet fieldValues = solution.eval(field);

                                for (A4Tuple fieldTuple : fieldValues) {
                                    if (fieldTuple.atom(0).equals(tuple.atom(0))) {
                                        String fieldName = field.label.substring(field.label.indexOf("/") + 1); // Get field name
                                        int fieldValue = Integer.parseInt(fieldTuple.atom(1).replace("Int$", ""));
                                        inputMap.put(fieldName, fieldValue);
                                    }
                                }
                            }

                            inputList.add(inputMap);
                        }
                    }
                }
            } else {
                System.out.println("UNSATISFIABLE!");
            }
        } catch (Err e) {
            System.err.println("Alloy error: " + e.getMessage());
        }

        return inputList;
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
            
            fact Preconditions {
                all i: Input | i.a != 0 || i.b != 0 
            }


            run {} for 50
        """;

        List<Map<String, Integer>> inputs = runAlloyModel(model);
        System.out.println("Inputs: " + inputs);
    }
}

