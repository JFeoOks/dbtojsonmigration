package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class QueryBuilderTest {

    // TODO: 03.09.2018 Test should be independent from counter.
    private final String accounts = "accounts";

    @Test
    public void testQueryOneTable() throws IOException {
        Assertions.assertEquals(
                new SelectBuilder().from(accounts +" a0").toString(),
                new QueryBuilder().createQueries("queryOneTableTest.json").get(0)
        );
    }

    @Test
    public void testQueryOneTableWithSpecifiedColumns() throws IOException {
        Assertions.assertEquals(
                new SelectBuilder().column("id").column("name").from(accounts + " a0").toString(),
                new QueryBuilder().createQueries("queryOneTableWithSpecifiedColumnsTest.json").get(0)
        );
    }

    @Test
    void testQueryTwoTable() {
        Assertions.assertIterableEquals(Arrays.asList(
                new SelectBuilder().from(accounts + " a0").toString(),
                new SelectBuilder()
                        .from("contacts c1")
                        .from(accounts + " a2")
                        .join("c1.account_id=a2.id").toString()
                ),
                new QueryBuilder().createQueries("queryTwoSeveralTableTest.json"));
    }
}

