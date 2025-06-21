package com.tomas.validator;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.*;
import kodkod.engine.satlab.SATFactory;

import java.util.*;


public class AlloyRunner {

    private DafnyToAlloyConverter dafnyToAlloyConverter;
    private static final int NUMBER_OF_INPUTS_FROM_ALLOY = 10;

    public AlloyRunner(DafnyToAlloyConverter dafnyToAlloyConverter) {
        this.dafnyToAlloyConverter = dafnyToAlloyConverter;
    }

    public Set<Map<String, Object>> runAlloyModel(String code) {
        String alloyModel = dafnyToAlloyConverter.convertToAlloyRun(code);

        if (alloyModel.contains("Error: Unsupported type")) {
            return Collections.emptySet();
        }

        Set<Map<String, Object>> inputList = new HashSet<>();

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
                                Map<String, Object> inputMap = new HashMap<>();

                                for (Sig.Field field : sig.getFields()) {
                                    A4TupleSet fieldValues = solution.eval(field);
                                    String fieldName = field.label.substring(field.label.indexOf("/") + 1);

                                    if (field.type().arity() == 3) { //é array
                                        Map<Integer, Object> array = new TreeMap<>();
                                        String typeString = "";
                                        for (A4Tuple fieldTuple : fieldValues) {
                                            if (fieldTuple.atom(0).equals(tuple.atom(0))) {
                                                int index = Integer.parseInt(fieldTuple.atom(1));
                                                String value = fieldTuple.atom(2);
                                                try {
                                                    int valueConverted = Integer.parseInt(value);
                                                    array.put(index, valueConverted);
                                                } catch (NumberFormatException e) {
                                                    String valueConverted = value.split("\\$")[0];
                                                    if (valueConverted.equals("boolean/True") || valueConverted.equals("boolean/False")) {
                                                        valueConverted = valueConverted.replace("boolean/", "");
                                                        array.put(index, valueConverted);
                                                    } else {
                                                        typeString = typeString + valueConverted;
                                                    }
                                                }
                                            }
                                        }
                                        if (!array.isEmpty()) {
                                            inputMap.put(fieldName, new ArrayList<>(array.values()));
                                        } else {
                                            inputMap.put(fieldName, typeString);
                                        }
                                    } else {
                                        for (A4Tuple fieldTuple : fieldValues) {
                                            if (fieldTuple.atom(0).equals(tuple.atom(0))) {
                                                int fieldValue = Integer.parseInt(fieldTuple.atom(1).replace("Int$", ""));
                                                inputMap.put(fieldName, fieldValue);
                                            }
                                        }
                                    }
                                }
                                inputList.add(inputMap);
                            }

                        }
                    }
                    solution = solution.next();
                } else {
                    System.out.println("UNSATISFIABLE!");
                    break;
                }
            }
        } catch (Err e) {
            System.err.println("Alloy error: " + e.getMessage());
        }

        return inputList;
    }
}

