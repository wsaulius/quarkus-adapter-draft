package com.example.adapter.api.admin;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.engine.RouteRegistry;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Maps runtime registry state into typed admin API responses.
 */
@ApplicationScoped
public class AdminResponseMapper {

    public AdminRoutesResponse toRoutesResponse(RouteRegistry registry, AdapterConfig config) {
        List<RouteSummary> routeSummaries = registry.all().stream()
                .map(this::toRouteSummary)
                .toList();

        return new AdminRoutesResponse(
                registry.loaded(),
                registry.size(),
                registry.source(),
                config.execution().mockEnabled() ? "mock" : "real",
                registry.failure(),
                routeSummaries
        );
    }

    private RouteSummary toRouteSummary(CompiledRoute route) {
        return new RouteSummary(
                route.route().tenant(),
                route.route().environment(),
                route.route().inputMethod(),
                route.route().inputPathTemplate(),
                route.route().planId(),
                route.plan().steps().size()
        );
    }
}
