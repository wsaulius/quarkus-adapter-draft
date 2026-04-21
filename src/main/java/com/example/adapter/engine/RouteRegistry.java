package com.example.adapter.engine;

import com.example.adapter.core.OrchestrationGraph;
import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.RouteKey;
import com.example.adapter.excel.error.MappingLoadFailure;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class RouteRegistry {
    private final AtomicReference<OrchestrationGraph> graphRef =
            new AtomicReference<>(new OrchestrationGraph(List.of()));
    private final AtomicReference<String> sourceRef = new AtomicReference<>("uninitialized");
    private final AtomicReference<MappingLoadFailure> failureRef = new AtomicReference<>(null);

    public void replaceAll(OrchestrationGraph graph, String source) {
        graphRef.set(graph);
        sourceRef.set(source);
        failureRef.set(null);
    }

    public void recordFailure(MappingLoadFailure failure, String source) {
        graphRef.set(new OrchestrationGraph(List.of()));
        sourceRef.set(source);
        failureRef.set(failure);
    }

    public Optional<RouteCandidate> find(RouteKey key, String path) {
        for (CompiledRoute route : graphRef.get().routes()) {
            if (!route.routeKeyPredicate().test(key)) {
                continue;
            }
            var match = route.match(path);
            if (match.isPresent()) {
                return Optional.of(new RouteCandidate(route, match.get()));
            }
        }
        return Optional.empty();
    }

    public boolean loaded() {
        return !graphRef.get().routes().isEmpty();
    }

    public int size() {
        return graphRef.get().routes().size();
    }

    public String source() {
        return sourceRef.get();
    }

    public java.util.List<CompiledRoute> all() {
        return graphRef.get().routes();
    }

    public MappingLoadFailure failure() {
        return failureRef.get();
    }

    public OrchestrationGraph graph() {
        return graphRef.get();
    }
}
