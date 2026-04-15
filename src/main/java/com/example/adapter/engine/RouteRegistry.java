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

    public void replaceAll(List<CompiledRoute> routes, String source) { routesRef.set(List.copyOf(routes)); sourceRef.set(source); }
    public List<CompiledRoute> all() { return routesRef.get(); }
    public int size() { return routesRef.get().size(); }
    public String source() { return sourceRef.get(); }
    public boolean loaded() { return !routesRef.get().isEmpty(); }

    public Optional<CompiledRoute> find(RouteContext ctx) {
        return routesRef.get().stream().filter(route -> matches(route, ctx)).findFirst();
    }

    private boolean matches(CompiledRoute route, RouteContext ctx) {
        var c = route.criteria();
        return eq(c.tenant(), ctx.tenant()) && eq(c.environment(), ctx.environment()) && eq(c.resource(), ctx.resource()) && eq(c.httpMethod(), ctx.httpMethod()) && eq(c.inputValue(), ctx.lookup(c.inputKey()));
    }

    private boolean eq(String expected, String actual) {
        return expected == null || expected.isBlank() || expected.equalsIgnoreCase(actual);
    }
}
