package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.*;

public class QueryBuilderTest {

    // TODO: 03.09.2018 Test should be independent from counter.
    private final String accounts = "accounts";

    @Test
    public void testQueryOneTable() {
        Assertions.assertEquals(
                new SelectBuilder().from(accounts +" a0").toString(),
                new QueryBuilder().createQueries("queryOneTableTest.json").get(0)
        );
    }

    @Test
    public void testQueryOneTableWithSpecifiedColumns() {
        Assertions.assertEquals(
                new SelectBuilder().column("id").column("name").from(accounts + " a0").toString(),
                new QueryBuilder().createQueries("queryOneTableWithSpecifiedColumnsTest.json").get(0)
        );
    }

    @Test
    void testQueryTwoTable() {
        Assertions.assertIterableEquals(asList(
                new SelectBuilder().from(accounts + " a0").toString(),
                new SelectBuilder()
                        .from("contacts c1")
                        .from(accounts + " a2")
                        .join("c1.account_id=a2.id").toString()
                ),
                new QueryBuilder().createQueries("queryTwoSeveralTableTest.json"));
    }

    @Test
    void testQueryTwoChildren() {
        Assertions.assertIterableEquals(asList(
                new SelectBuilder().from(accounts + " a0").toString(),
                new SelectBuilder()
                        .from("contacts c1")
                        .from(accounts + " a2")
                        .join("c1.account_id=a2.id").toString(),
                new SelectBuilder()
                        .from("devices d3")
                        .from(accounts + " a4")
                        .join("d3.account_id=a4.id").toString()),
                new QueryBuilder().createQueries("querySeveralChildren.json"));

    }

    @Test
    void takEtoNRabotaet() {
        System.out.println(new QueryBuilder().createQueries("queryMultipleLevel.json")
                .stream().map(Object::toString).reduce((s1, s2) -> s1 + '\n' + s2).get());

    }
}


