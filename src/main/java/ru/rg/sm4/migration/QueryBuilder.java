package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {


    private long counter = 0;

    private final TableConfiguration configuration;

    public QueryBuilder(TableConfiguration configuration) {
        this.configuration = configuration;
    }


    public List<String> createQueries() {
        return covertHierarchyToQueries(configuration);
    }


    private List<String> covertHierarchyToQueries(TableConfiguration config) {
        List<String> result = new ArrayList<>();
        String currentAlias = getTableAlias(config.getTable());
        SelectBuilder query = new SelectBuilder()
                .from(applyAliasToTable(config.getTable(), currentAlias));
        SelectBuilder clonedQuery = query.clone();
        config.getColumns().forEach(column -> query.column(currentAlias  + "." + column));
        result.add(query.toString());
        result.addAll(processHierarchy(config, clonedQuery));
        return result;
    }

    private List<String> processHierarchy(TableConfiguration config, SelectBuilder query) {
        List<String> result = new ArrayList<>();
        String currentAlias = getNextTableAlias(config.getTable());

        for (TableConfiguration child : config.getChildren()) {
            if (child.getLink() == null) continue;

            String childAlias = getTableAlias(child.getTable());
            SelectBuilder tmpQuery = query.clone();
            tmpQuery
                    .from(applyAliasToTable(child.getTable(), childAlias))
                    .join(childAlias + "." + child.getLink().getColumn() +
                            "=" + currentAlias + "." + child.getLink().getWith());
            child.getColumns().forEach(tmpQuery::column);
            result.add(tmpQuery.toString());
            result.addAll(processHierarchy(child,tmpQuery /*query.clone()*/));
        }

        return result;
    }

    private String getNextTableAlias(String table) {
        return table.substring(0, 1) + counter++;
    }

    private String getTableAlias(String table) {
        return table.substring(0, 1) + counter;
    }

    private String applyAliasToTable(String config, String currentAlias) {
        return config + " " + currentAlias;
    }
}
