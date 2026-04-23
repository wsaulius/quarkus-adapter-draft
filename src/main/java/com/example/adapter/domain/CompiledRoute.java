package com.example.adapter.domain;

import com.example.adapter.template.InboundPathMatcher;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public record CompiledRoute(RouteRow route, Predicate<RouteKey> routeKeyPredicate, InboundPathMatcher matcher,
                            CompiledPlan plan) {
    public Optional<Map<String, String>> match(String path) {
        return Optional.ofNullable(matcher.match(path));
    }
}
