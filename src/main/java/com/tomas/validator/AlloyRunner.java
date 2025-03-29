package com.tomas.validator;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.*;
import kodkod.engine.satlab.SATFactory;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class AlloyRunner {

    private DafnyToAlloyConverter dafnyToAlloyConverter;
    private RestTemplate restTemplate;
    private static final int NUMBER_OF_INPUTS_FROM_ALLOY = 10;

    public AlloyRunner(DafnyToAlloyConverter dafnyToAlloyConverter, RestTemplate restTemplate) {
        this.dafnyToAlloyConverter = dafnyToAlloyConverter;
        this.restTemplate = restTemplate;
    }

    public Set<Map<String, Integer>> runAlloyModel(String code) {
        String alloyModel = dafnyToAlloyConverter.convertToAlloy(code);
        Set<Map<String, Integer>> inputList = new HashSet<>();

        try {
            A4Reporter rep = new A4Reporter();
            CompModule world = CompUtil.parseEverything_fromString(rep, alloyModel);
            A4Options options = new A4Options();
            options.solver = SATFactory.get("minisat");

            Command cmd = world.getAllCommands().get(0);

            A4Solution solution = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd, options);
            while (inputList.size() < NUMBER_OF_INPUTS_FROM_ALLOY) {
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
                                            String fieldName = field.label.substring(field.label.indexOf("/") + 1);
                                            int fieldValue = Integer.parseInt(fieldTuple.atom(1).replace("Int$", ""));
                                            inputMap.put(fieldName, fieldValue);
                                        }
                                    }
                                }
                                if (!inputList.contains(inputMap)) {
                                    inputList.add(inputMap);
                                }
                            }

                        }
                    }
                    solution = solution.next();
                } else {
                    System.out.println("UNSATISFIABLE!");
                }
            }
        } catch (Err e) {
            System.err.println("Alloy error: " + e.getMessage());
        }

        return inputList;
    }

}

