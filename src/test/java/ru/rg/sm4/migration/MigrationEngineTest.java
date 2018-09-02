package ru.rg.sm4.migration;

import ca.krasnay.sqlbuilder.SelectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MigrationEngineTest {

    @Test
    public void testQueryOneTable() throws IOException {
        Assertions.assertEquals(
                new SelectBuilder().from("accounts").toString(),
                new MigrationEngine().createQuery("queryOneTableTest.json")
        );
    }

    @Test
    public void testQueryOneTableWithSpecifiedColumns() throws IOException {
        Assertions.assertEquals(
                new SelectBuilder().column("id").column("name").from("accounts").toString(),
                new MigrationEngine().createQuery("queryOneTableWithSpecifiedColumnsTest.json")
        );
    }
}
