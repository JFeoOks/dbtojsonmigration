package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static ru.rg.sm4.migration.ConfiguratonLoader.loadConfiguration;

public class QueryBuilderTest {

    // TODO: 03.09.2018 Test should be independent from counter.
    private final String accounts = "accounts";

    @Test
    public void testQueryOneTable() {
        Assertions.assertEquals(
                new SelectBuilder().from(accounts + " a0").toString(),
                new QueryBuilder(loadConfiguration("queryOneTableTest.json")).createQueries().get(0)
        );
    }

    @Test
    public void testQueryOneTableWithSpecifiedColumns() {
        String alias = "a0";
        Assertions.assertEquals(
                new SelectBuilder().column(alias +".id").column(alias +".name").from(accounts + " " + alias).toString(),
                new QueryBuilder(loadConfiguration("queryOneTableWithSpecifiedColumnsTest.json")).createQueries().get(0)
        );
    }

    @Test
    void testQueryTwoTable() {
        Assertions.assertIterableEquals(asList(
                new SelectBuilder().from(accounts + " a0").toString(),
                new SelectBuilder()
                        .from(accounts + " a0")
                        .from("contacts c1")
                        .join("c1.account_id=a0.id").toString()
                ),
                new QueryBuilder(loadConfiguration("queryTwoSeveralTableTest.json")).createQueries());
    }

    @Test
    void testQueryTwoChildren() {
        Assertions.assertIterableEquals(asList(
                new SelectBuilder().from(accounts + " a0").toString(),
                new SelectBuilder()
                        .from(accounts + " a0")
                        .from("contacts c1")
                        .join("c1.account_id=a0.id").toString(),
                new SelectBuilder()
                        .from(accounts + " a0")
                        .from("devices d2")
                        .join("d2.account_id=a0.id").toString()),
                new QueryBuilder(loadConfiguration("querySeveralChildren.json")).createQueries());

    }

    @Test
    void testTableHierarchyProcessing() {
        List<String> expected = Arrays.asList(
                new SelectBuilder()
                        .from("accounts a0")
                        .toString(),
                new SelectBuilder()
                        .from("accounts a0")
                        .from("contacts c1")
                        .join("c1.account_id=a0.id")
                        .toString(),
                new SelectBuilder()
                        .from("accounts a0")
                        .from("contacts c1")
                        .from("child_1 c2")
                        .join("c1.account_id=a0.id")
                        .join("c2.child_pk_1=c1.id")
                        .toString()
        );

        List<String> actual = new QueryBuilder(loadConfiguration("queryMultipleLevel.json")).createQueries();
        Assertions.assertIterableEquals(expected, actual);
    }
}


