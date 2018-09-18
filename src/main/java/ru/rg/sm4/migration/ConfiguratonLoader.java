package ru.rg.sm4.migration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;

public class ConfiguratonLoader {

    private final static ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static TableConfiguration loadConfiguration(String resource){
        return mapper.readValue(getCurrentResource(resource),
                TableConfiguration.class
        );

    }

    private static InputStream getCurrentResource(String resource) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }

}
