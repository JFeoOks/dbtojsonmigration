package ru.rg.sm4.migration;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TableConfiguration {
    private String table;

    private List<String> columns = new ArrayList<>();
    private List<TableConfiguration> children = new ArrayList<>();
    /**
     * Should be null on parent node
     */
    private JoinLink link;
}