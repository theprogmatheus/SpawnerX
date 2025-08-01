package com.github.theprogmatheus.mc.plugin.spawnerx.util;

public class StringUtils {

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char firstChar = input.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return input;
        }

        return Character.toUpperCase(firstChar) + input.substring(1);
    }

    public static String normalize(String input) {
        if (input == null) return null;

        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        normalized = normalized.replaceAll("[^\\p{Alnum}_\\s]", ""); // permite "_" aqui
        normalized = normalized.trim().replaceAll("\\s+", " ");
        return normalized.toLowerCase();
    }

    public static String prettifyEntityName(String rawName) {
        if (rawName == null || rawName.isEmpty()) return rawName;

        String normalized = normalize(rawName);
        String[] parts = normalized.split("_");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            builder.append(capitalize(parts[i]));
            if (i < parts.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
}
