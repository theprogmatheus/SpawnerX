package com.github.theprogmatheus.mc.plugin.spawnerx.database;

import lombok.Data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Data
public class SqlQueryLoader {

    private final String path;
    private final String tablePrefix;
    private final Map<String, String> cachedQueries;

    public List<String> getQueries(String queryPath) {
        String query = getQuery(queryPath);
        if (query != null)
            return Arrays.stream(query.split(";"))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .collect(Collectors.toList());
        return List.of();
    }

    public String getQuery(String queryPath) {
        return this.cachedQueries
                .computeIfAbsent(queryPath, key -> loadQuery(queryPath));
    }

    private String loadQuery(String queryPath) {
        InputStream resource = getClass().getResourceAsStream("%s/%s.sql".formatted(this.path, queryPath));
        if (resource == null)
            throw new IllegalArgumentException("The query '%s' not found.".formatted(queryPath));


        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append(System.lineSeparator());
            }
        }

        var result = stringBuilder.toString()
                .replace("%table_prefix%", this.tablePrefix)
                .trim();

        if (result.isBlank())
            throw new IllegalStateException("The query for address %s is null or empty.".formatted(queryPath));

        return result;
    }
}