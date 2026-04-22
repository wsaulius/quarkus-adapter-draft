/**
 * Loads and registers workbook configuration at application startup.
 *
 * <p>This startup bean reads the configured workbook, invokes the loader and compiler,
 * and publishes the resulting graph into the registry.
 *
 * <p>If loading fails, the failure is preserved with a precise message and Excel reference
 * so readiness endpoints can report the problem clearly.
 */
package com.example.adapter.engine;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.excel.ExcelWorkbookLoader;
import com.example.adapter.excel.WorkbookCompiler;
import com.example.adapter.excel.error.ExcelMappingException;
import com.example.adapter.excel.error.MappingLoadFailure;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;

@Startup
@ApplicationScoped
public class MappingBootstrap {
    @Inject
    AdapterConfig config;

    @Inject
    ExcelWorkbookLoader loader;

    @Inject
    WorkbookCompiler compiler;

    @Inject
    RouteRegistry registry;

    @PostConstruct
    void init() {
        String source = config.excel().source();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(source)) {
            if (is == null) {
                throw new IllegalStateException("Excel file not found on classpath: " + source);
            }
            registry.replaceAll(compiler.compile(loader.load(is)), "classpath:" + source);
        } catch (ExcelMappingException e) {
            registry.recordFailure(new MappingLoadFailure(e.code(), e.getMessage(), e.location().asRef()), "classpath:" + source);
        } catch (Exception e) {
            registry.recordFailure(new MappingLoadFailure("BOOTSTRAP_ERROR", e.getMessage(), source), "classpath:" + source);
        }
    }
}
