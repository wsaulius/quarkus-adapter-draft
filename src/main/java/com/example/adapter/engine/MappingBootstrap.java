package com.example.adapter.engine;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.excel.ExcelMappingLoader;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.InputStream;

@ApplicationScoped
public class MappingBootstrap {
    @Inject AdapterConfig config;
    @Inject ExcelMappingLoader loader;
    @Inject RouteRegistry registry;

    @PostConstruct
    void init() {
        String source = config.excel().source();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(source)) {
            if (is == null) throw new IllegalStateException("Excel file not found on classpath: " + source);
            registry.replaceAll(loader.load(is, config.excel().sheet()), "classpath:" + source);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize mappings", e);
        }
    }
}
