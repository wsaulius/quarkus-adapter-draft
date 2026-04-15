package com.example.adapter.engine;

import com.example.adapter.domain.CompiledRoute;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class RouteRegistry {
    private final AtomicReference<List<CompiledRoute>> routesRef = new AtomicReference<>(List.of());
    private final AtomicReference<String> sourceRef = new AtomicReference<>("uninitialized");

    public void replaceAll(List<CompiledRoute> routes, String source) {
        routesRef.set(List.copyOf(routes));
        sourceRef.set(source);
    }

    public boolean loaded() { return !routesRef.get().isEmpty(); }
    public int size() { return routesRef.get().size(); }
    public String source() { return sourceRef.get(); }
    public List<CompiledRoute> all() { return routesRef.get(); }

    public Optional<RouteMatch> find(RouteContext ctx) {
        for (CompiledRoute route : routesRef.get()) {
            if (!eq(route.definition().tenant(), ctx.tenant())) continue;
            if (!eq(route.definition().environment(), ctx.environment())) continue;
            if (!eq(route.definition().inputMethod(), ctx.httpMethod())) continue;
            var params = route.extract(ctx.fullPath());
            if (params != null) return Optional.of(new RouteMatch(route, params));
        }
        return Optional.empty();
    }

    private boolean eq(String expected, String actual) {
        return expected == null || expected.isBlank() || expected.equalsIgnoreCase(actual);
    }
}
