package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private static ObjectMapper mapper = new ObjectMapper();

    private long counter = 0;

    public List<String> createQueries(String resource) {
        TableConfiguration config = readConfiguration(resource);
        return createRootTableQuery(config);
    }

    @SneakyThrows
    private TableConfiguration readConfiguration(String resource) {
        return mapper.readValue(getCurrentResource(resource),
                TableConfiguration.class
        );
    }

    private InputStream getCurrentResource(String resource) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }

    private List<String> createRootTableQuery(TableConfiguration config) {
        List<String> result = new ArrayList<>();
        String currentAlias = createRootAlias(config.getTable());
        SelectBuilder query = new SelectBuilder()
                .from(applyAliasToTable(config.getTable(), currentAlias));
//        config.getColumns().forEach(query::column);

        result.add(query.toString());

        result.addAll(createTableQuery(config, query.clone()));
        return result;
    }

    private List<String> createTableQuery(TableConfiguration config, SelectBuilder query) {
        List<String> result = new ArrayList<>();
        String currentAlias = createAlias(config.getTable());
        SelectBuilder tmpQuery = query.clone();

        for (TableConfiguration child : config.getChildren()) {
            if (child.getLink() == null) continue;

            String childAlias = createRootAlias(child.getTable());
//            child.getColumns().forEach(query::column);
            query
                    .from(applyAliasToTable(child.getTable(), childAlias))
                    .join(childAlias + "." + child.getLink().getColumn() +
                            "=" + currentAlias + "." + child.getLink().getWith());

            result.add(query.toString());
            result.addAll(createTableQuery(child, query.clone()));
            query = tmpQuery;
        }

        return result;
    }

    private String createAlias(String table) {
        return table.substring(0, 1) + counter++;
    }

    private String createRootAlias(String table) {
        return table.substring(0, 1) + counter;
    }

    private String applyAliasToTable(String config, String currentAlias) {
        return config + " " + currentAlias;
    }
}
