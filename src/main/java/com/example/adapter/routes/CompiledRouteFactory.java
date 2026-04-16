package com.example.adapter.routes;

import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.CompiledTransform;
import com.example.adapter.domain.ExcelRouteDefinition;
import com.example.adapter.domain.RouteKey;
import com.example.adapter.dsl.DslRegistry;
import com.example.adapter.excel.error.ExcelLocation;
import com.example.adapter.excel.error.ExcelRouteCompilationException;
import com.example.adapter.excel.error.ExcelUnknownTransformReferenceException;
import com.example.adapter.template.PathTemplate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@ApplicationScoped
public class CompiledRouteFactory {
    @Inject DslRegistry dslRegistry;

    public CompiledRoute compile(ExcelRouteDefinition def, String sheetName, int rowIndex, int transformRefColumnIndex, String routeId) {
        try {
            PathTemplate inbound = new PathTemplate(def.inputPathTemplate());
            PathTemplate outbound = new PathTemplate(def.targetPathTemplate());
            Predicate<RouteKey> routeKeyPredicate = key ->
                    eq(def.tenant(), key.tenant()) &&
                    eq(def.environment(), key.environment()) &&
                    eq(def.inputMethod(), key.method());
            Function<String, Optional<Map<String, String>>> matcher = path -> Optional.ofNullable(inbound.match(path));
            Function<Map<String, String>, String> renderer = outbound::render;
            CompiledTransform transform;
            try {
                transform = dslRegistry.transform(def.transformRef());
            } catch (IllegalArgumentException e) {
                throw new ExcelUnknownTransformReferenceException(sheetName, rowIndex, transformRefColumnIndex, def.transformRef());
            }
            return new CompiledRoute(def, routeKeyPredicate, matcher, renderer, transform);
        } catch (ExcelUnknownTransformReferenceException e) {
            throw e;
        } catch (Exception e) {
            throw new ExcelRouteCompilationException(
                    "Failed to compile route '" + routeId + "' at " + new ExcelLocation(sheetName, rowIndex, 0).asRef() + ": " + e.getMessage(),
                    routeId,
                    new ExcelLocation(sheetName, rowIndex, 0),
                    e
            );
        }
    }

    private boolean eq(String expected, String actual) {
        return expected == null || expected.isBlank() || expected.equalsIgnoreCase(actual);
    }
}
