package com.example.adapter.engine;

import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.RouteKey;
import com.example.adapter.excel.error.MappingLoadFailure;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class RouteRegistry {
    private final AtomicReference<List<CompiledRoute>> routesRef = new AtomicReference<>(List.of());
    private final AtomicReference<String> sourceRef = new AtomicReference<>("uninitialized");
    private final AtomicReference<MappingLoadFailure> failureRef = new AtomicReference<>(null);

    public void replaceAll(List<CompiledRoute> routes, String source) {
        routesRef.set(List.copyOf(routes));
        sourceRef.set(source);
        failureRef.set(null);
    }

    public void recordFailure(MappingLoadFailure failure, String source) {
        routesRef.set(List.of());
        sourceRef.set(source);
        failureRef.set(failure);
    }

    public Optional<RouteCandidate> find(RouteKey key, String path) {
        for (CompiledRoute route : routesRef.get()) {
            if (!route.routeKeyPredicate().test(key)) continue;
            var match = route.inboundMatcher().apply(path);
            if (match.isPresent()) return Optional.of(new RouteCandidate(route, match.get()));
        }
        return Optional.empty();
    }

    public boolean loaded() { return !routesRef.get().isEmpty(); }
    public int size() { return routesRef.get().size(); }
    public String source() { return sourceRef.get(); }
    public List<CompiledRoute> all() { return routesRef.get(); }
    public MappingLoadFailure failure() {

        try {
            return Optional.ofNullable(failureRef.get()).get();
        } catch ( NoSuchElementException e ) {
            return new MappingLoadFailure("200","OK", "none");
        }

    }
}
