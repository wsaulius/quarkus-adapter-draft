package com.example.adapter.engine;

import com.example.adapter.domain.*;
import com.example.adapter.excel.error.MappingLoadFailure;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class RouteRegistry {
    private final AtomicReference<List<CompiledRoute>> routesRef = new AtomicReference<>(List.of());
    private final AtomicReference<String> sourceRef = new AtomicReference<>("uninitialized");
    private final AtomicReference<MappingLoadFailure> failureRef = new AtomicReference<>(null);

    public void replaceAll(List<CompiledRoute> routes, String source) { routesRef.set(List.copyOf(routes)); sourceRef.set(source); failureRef.set(null); }
    public void recordFailure(MappingLoadFailure f, String source) { routesRef.set(List.of()); sourceRef.set(source); failureRef.set(f); }

    public Optional<RouteCandidate> find(RouteKey key, String path) {
        for (CompiledRoute route : routesRef.get()) {
            if (!route.routeKeyPredicate().test(key)) continue;
            var m = route.match(path);
            if (m.isPresent()) return Optional.of(new RouteCandidate(route, m.get()));
        }
        return Optional.empty();
    }

    public boolean loaded() { return !routesRef.get().isEmpty(); }
    public int size() { return routesRef.get().size(); }
    public String source() { return sourceRef.get(); }
    public List<CompiledRoute> all() { return routesRef.get(); }
    public MappingLoadFailure failure() { return failureRef.get(); }
}
