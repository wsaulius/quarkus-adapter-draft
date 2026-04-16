package com.example.adapter.domain;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public record CompiledRoute(
        ExcelRouteDefinition definition,
        Predicate<RouteKey> routeKeyPredicate,
        Function<String, Optional<Map<String, String>>> inboundMatcher,
        Function<Map<String, String>, String> outboundRenderer,
        CompiledTransform transform
) {}
