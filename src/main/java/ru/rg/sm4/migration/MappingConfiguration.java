package ru.rg.sm4.migration;

import java.util.List;

public class MappingConfiguration {

    private String table;
    private List<String> columns;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
