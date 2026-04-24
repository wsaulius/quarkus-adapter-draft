package com.example.adapter.api.admin;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.core.OrchestrationGraph;
import com.example.adapter.domain.CompiledField;
import com.example.adapter.domain.CompiledPlan;
import com.example.adapter.domain.CompiledPlanStep;
import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.engine.RouteRegistry;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public AdminPlansResponse toPlansResponse(RouteRegistry registry, AdapterConfig config) {
        List<PlanSummary> plans = uniquePlans(registry.graph()).values().stream()
                .map(this::toPlanSummary)
                .toList();

        return new AdminPlansResponse(
                registry.loaded(),
                plans.size(),
                registry.source(),
                config.execution().mockEnabled() ? "mock" : "real",
                registry.failure(),
                plans
        );
    }

    public VerboseAdminGraphResponse toGraphResponse(RouteRegistry registry, AdapterConfig config) {
        Map<String, CompiledPlan> plansById = uniquePlans(registry.graph());

        List<GraphRouteSummary> routes = registry.all().stream()
                .map(this::toGraphRouteSummary)
                .toList();

        List<GraphPlanSummary> plans = plansById.values().stream()
                .map(this::toGraphPlanSummary)
                .toList();

        return new VerboseAdminGraphResponse(
                registry.loaded(),
                registry.size(),
                plansById.size(),
                registry.source(),
                config.execution().mockEnabled() ? "mock" : "real",
                registry.failure(),
                routes,
                plans
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

    private PlanSummary toPlanSummary(CompiledPlan plan) {
        return new PlanSummary(
                plan.id(),
                plan.steps().size(),
                plan.steps().stream().map(CompiledPlanStep::id).toList(),
                plan.aggregator().fields().stream().map(CompiledField::targetField).toList()
        );
    }

    private GraphRouteSummary toGraphRouteSummary(CompiledRoute route) {
        return new GraphRouteSummary(
                route.route().tenant(),
                route.route().environment(),
                route.route().inputMethod(),
                route.route().inputPathTemplate(),
                route.route().targetSystem(),
                route.route().planId(),
                route.plan().steps().size()
        );
    }

    private GraphPlanSummary toGraphPlanSummary(CompiledPlan plan) {
        return new GraphPlanSummary(
                plan.id(),
                plan.steps().size(),
                plan.steps().stream().map(this::toGraphStepSummary).toList(),
                plan.aggregator().fields().stream().map(CompiledField::targetField).toList()
        );
    }

    private GraphStepSummary toGraphStepSummary(CompiledPlanStep step) {
        return new GraphStepSummary(
                step.id(),
                step.providerCall(),
                step.method(),
                step.baseUrl(),
                step.pathTemplate(),
                step.transform() == null ? null : step.transform().name(),
                step.stopOnError(),
                step.timeoutMs(),
                step.transform() == null ? List.of() : step.transform().fields().stream().map(CompiledField::targetField).toList()
        );
    }

    private Map<String, CompiledPlan> uniquePlans(OrchestrationGraph graph) {
        Map<String, CompiledPlan> plans = new LinkedHashMap<>();
        graph.routes().forEach(route -> plans.putIfAbsent(route.plan().id(), route.plan()));
        return plans;
    }
}
