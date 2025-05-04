package com.tomas.util;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<String> extractVariableNames(String input) {
        List<String> names = new ArrayList<>();

        if (input.trim().isEmpty()) return names;

        String[] parts = input.split(",");

        for (String part : parts) {
            String[] pair = part.trim().split(":");
            if (pair.length > 0) {
                names.add(pair[0].trim());
            }
        }
        return names;
    }
}
