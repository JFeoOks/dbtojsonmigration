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
        String masterTable = createTableQuery(config, null);
        String previousTable = config.getTable();
        List<String> queries = processChildrenElements(config, previousTable);
        queries.add(0, masterTable);
        return queries;
    }

    private List<String> processChildrenElements(TableConfiguration config, String previousTable) {
        List<String> result = new ArrayList<>();
        for (TableConfiguration child : config.getChildren()) {
            result.add(createTableQuery(child, previousTable));
            result.addAll(processChildrenElements(child, config.getTable()));
        }
        return result;
    }

    private String createTableQuery(TableConfiguration config, String parent) {
        String currentAlias = createAlias(config.getTable());
        SelectBuilder query = new SelectBuilder().from(applyAliasToTable(config.getTable(), currentAlias));
        List<String> columns = config.getColumns();
        columns.forEach(query::column);

        JoinLink link = config.getLink();
        if (link != null) {
            String parentAlias = createAlias(parent);
            query.from(applyAliasToTable(parent, parentAlias));
            query.join(currentAlias + "." + link.getColumn() + "=" + parentAlias + "." + link.getWith());
        }
        return query.toString();
    }

    private String applyAliasToTable(String config, String currentAlias) {
        return config + " " + currentAlias;
    }

    private String createAlias(String table) {
        return table.substring(0, 1) + counter++;
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
}
