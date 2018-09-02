package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class MigrationEngine {

    private static ObjectMapper mapper = new ObjectMapper();

    public String createQuery(String resource) throws IOException {
        MappingConfiguration config = mapper.readValue(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(resource),
                MappingConfiguration.class
        );

        SelectBuilder query = new SelectBuilder().from(config.getTable());

        List<String> columns = config.getColumns();

        if (columns != null) {
            for (String column : columns) {
                query.column(column);
            }
        }

        return query.toString();
    }
}
