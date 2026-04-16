package com.example.adapter.domain;

import com.example.adapter.expression.FieldMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public record CompiledRoute(
        ExcelRouteDefinition definition,
        Predicate<RouteKey> routeKeyPredicate,
        Function<String, Optional<Map<String, String>>> inboundMatcher,
        Function<Map<String, String>, String> outboundRenderer,
        List<FieldMapping> compiledRequestMapping
) {}
