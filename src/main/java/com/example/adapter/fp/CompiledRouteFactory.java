package com.example.adapter.fp;

import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.ExcelRouteDefinition;
import com.example.adapter.domain.RouteKey;
import com.example.adapter.expression.FieldMapping;
import com.example.adapter.expression.MappingExpressionCompiler;
import com.example.adapter.template.PathTemplate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@ApplicationScoped
public class CompiledRouteFactory {

    @Inject
    MappingExpressionCompiler expressionCompiler;

    public CompiledRoute compile(ExcelRouteDefinition def) {
        PathTemplate inbound = new PathTemplate(def.inputPathTemplate());
        PathTemplate outbound = new PathTemplate(def.targetPathTemplate());

        Predicate<RouteKey> routeKeyPredicate = key ->
                eq(def.tenant(), key.tenant()) &&
                eq(def.environment(), key.environment()) &&
                eq(def.inputMethod(), key.method());

        Function<String, Optional<Map<String, String>>> matcher = path ->
                Optional.ofNullable(inbound.match(path));

        Function<Map<String, String>, String> renderer = outbound::render;

        List<FieldMapping> compiledRequestMapping = expressionCompiler.compile(def.requestMapping());

        return new CompiledRoute(
                def,
                routeKeyPredicate,
                matcher,
                renderer,
                compiledRequestMapping
        );
    }

    private boolean eq(String expected, String actual) {
        return expected == null || expected.isBlank() || expected.equalsIgnoreCase(actual);
    }
}
