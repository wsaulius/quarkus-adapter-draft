package com.example.adapter.excel;

import com.example.adapter.domain.*;
import com.example.adapter.expression.*;
import com.example.adapter.template.InboundPathMatcher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.function.Predicate;

@ApplicationScoped
public class WorkbookCompiler {
    @Inject ExpressionCompiler expressionCompiler;

    public List<CompiledRoute> compile(WorkbookModel model) {
        Map<String, CompiledTransform> transforms = compileTransforms(model.transforms());
        Map<String, List<CompiledPlanStep>> steps = compileSteps(model.steps(), transforms);
        Map<String, CompiledAggregator> aggs = compileAggregates(model.aggregates());

        List<CompiledRoute> routes = new ArrayList<>();
        for (RouteRow row : model.routes()) {
            CompiledPlan plan = new CompiledPlan(row.planId(), steps.getOrDefault(row.planId(), List.of()), aggs.getOrDefault(row.planId(), new CompiledAggregator(List.of())));
            Predicate<RouteKey> predicate = k ->
                    row.tenant().equalsIgnoreCase(k.tenant()) &&
                    row.environment().equalsIgnoreCase(k.environment()) &&
                    row.inputMethod().equalsIgnoreCase(k.method());
            routes.add(new CompiledRoute(row, predicate, new InboundPathMatcher(row.inputPathTemplate()), plan));
        }
        routes.sort(Comparator.comparingInt((CompiledRoute r) -> r.route().priority()).reversed());
        return routes;
    }

    private Map<String, CompiledTransform> compileTransforms(List<TransformFieldRow> rows) {
        Map<String, List<CompiledField>> grouped = new LinkedHashMap<>();
        for (TransformFieldRow r : rows) {
            grouped.computeIfAbsent(r.transformRef(), k -> new ArrayList<>())
                    .add(new CompiledField(r.targetField(), toExpr(r.sourceExpr()), r.optional()));
        }
        Map<String, CompiledTransform> out = new LinkedHashMap<>();
        grouped.forEach((k,v) -> out.put(k, new CompiledTransform(k, List.copyOf(v))));
        return out;
    }

    private Map<String, List<CompiledPlanStep>> compileSteps(List<StepRow> rows, Map<String, CompiledTransform> transforms) {
        Map<String, List<CompiledPlanStep>> grouped = new LinkedHashMap<>();
        for (StepRow r : rows) {
            grouped.computeIfAbsent(r.planId(), k -> new ArrayList<>())
                    .add(new CompiledPlanStep(r.stepId(), r.stepOrder(), r.method(), r.baseUrl(), r.pathTemplate(), transforms.get(r.transformRef()), r.stopOnError(), r.timeoutMs()));
        }
        grouped.values().forEach(list -> list.sort(Comparator.comparingInt(CompiledPlanStep::order)));
        return grouped;
    }

    private Map<String, CompiledAggregator> compileAggregates(List<AggregateFieldRow> rows) {
        Map<String, List<CompiledField>> grouped = new LinkedHashMap<>();
        for (AggregateFieldRow r : rows) {
            grouped.computeIfAbsent(r.planId(), k -> new ArrayList<>())
                    .add(new CompiledField(r.targetField(), toExpr(r.sourceExpr()), r.optional()));
        }
        Map<String, CompiledAggregator> out = new LinkedHashMap<>();
        grouped.forEach((k,v) -> out.put(k, new CompiledAggregator(List.copyOf(v))));
        return out;
    }

    private ExpressionDef toExpr(String s) {
        if (s.startsWith("$.steps.")) return new ExpressionDef(ExpressionKind.STEP, s.substring("$.steps.".length()));
        if (s.startsWith("$.path.")) return new ExpressionDef(ExpressionKind.PATH, s.substring("$.path.".length()));
        if (s.startsWith("$.")) return new ExpressionDef(ExpressionKind.BODY, s.substring(2));
        return new ExpressionDef(ExpressionKind.LITERAL, s);
    }
}
