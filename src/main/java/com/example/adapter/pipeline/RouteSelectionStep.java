package com.example.adapter.pipeline;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.engine.RouteRegistry;
import com.example.adapter.fp.ProcessingStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RouteSelectionStep implements ProcessingStep<ExecutionContext> {
    @Inject RouteRegistry registry;

    @Override
    public ExecutionContext apply(ExecutionContext context) {
        var candidate = registry.find(context.routeKey(), context.fullPath())
                .orElseThrow(() -> new IllegalArgumentException("No mapping found"));
        return context.withRoute(candidate.route(), candidate.pathParams());
    }
}
