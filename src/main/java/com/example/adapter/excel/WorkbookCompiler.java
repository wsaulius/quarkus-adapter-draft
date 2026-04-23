/**
 * Compiles workbook rows into an immutable orchestration graph.
 *
 * <p>This class sits between raw Excel data and runtime execution. It resolves workbook
 * rows into compiled transforms, compiled plan steps, compiled aggregators, and compiled routes.
 *
 * <p>Compilation rules:
 * <ul>
 *   <li>Transforms are grouped by transform reference</li>
 *   <li>Steps are grouped and irisCalled by plan id</li>
 *   <li>Aggregates are grouped by plan id</li>
 *   <li>Routes are matched to plans and wrapped with inbound path matchers</li>
 * </ul>
 *
 * <p>The output graph is immutable and optimized for request-time use.
 */
package com.example.adapter.excel;

import com.example.adapter.core.OrchestrationGraph;
import com.example.adapter.domain.AggregateFieldRow;
import com.example.adapter.domain.CompiledAggregator;
import com.example.adapter.domain.CompiledField;
import com.example.adapter.domain.CompiledPlan;
import com.example.adapter.domain.CompiledPlanStep;
import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.CompiledTransform;
import com.example.adapter.domain.RouteKey;
import com.example.adapter.domain.RouteRow;
import com.example.adapter.domain.StepRow;
import com.example.adapter.domain.TransformFieldRow;
import com.example.adapter.expression.ExpressionParser;
import com.example.adapter.template.InboundPathMatcher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@ApplicationScoped
public class WorkbookCompiler {
    @Inject
    ExpressionParser expressionParser;

    public OrchestrationGraph compile(WorkbookModel model) {
        Map<String, CompiledTransform> transforms = compileTransforms(model.transforms());
        Map<String, List<CompiledPlanStep>> steps = compileSteps(model.steps(), transforms);
        Map<String, CompiledAggregator> aggs = compileAggregates(model.aggregates());

        List<CompiledRoute> routes = new ArrayList<>();
        for (RouteRow row : model.routes()) {
            CompiledPlan plan = new CompiledPlan(
                    row.planId(),
                    steps.getOrDefault(row.planId(), List.of()),
                    aggs.getOrDefault(row.planId(), new CompiledAggregator(List.of()))
            );
            Predicate<RouteKey> predicate = key ->
                    row.tenant().equalsIgnoreCase(key.tenant()) &&
                    row.environment().equalsIgnoreCase(key.environment()) &&
                    row.inputMethod().equalsIgnoreCase(key.method());

            routes.add(new CompiledRoute(row, predicate, new InboundPathMatcher(row.inputPathTemplate()), plan));
        }

        routes.sort(Comparator.comparingInt((CompiledRoute r) -> r.route().priority()).reversed());
        return new OrchestrationGraph(List.copyOf(routes));
    }

    private Map<String, CompiledTransform> compileTransforms(List<TransformFieldRow> rows) {
        Map<String, List<CompiledField>> grouped = new LinkedHashMap<>();
        for (TransformFieldRow row : rows) {
            grouped.computeIfAbsent(row.transformRef(), k -> new ArrayList<>())
                    .add(new CompiledField(row.targetField(), expressionParser.parse(row.sourceExpr()), row.optional()));
        }
        Map<String, CompiledTransform> out = new LinkedHashMap<>();
        grouped.forEach((k, v) -> out.put(k, new CompiledTransform(k, List.copyOf(v))));
        return out;
    }

    private Map<String, List<CompiledPlanStep>> compileSteps(List<StepRow> rows, Map<String, CompiledTransform> transforms) {
        Map<String, List<CompiledPlanStep>> grouped = new LinkedHashMap<>();
        for (StepRow row : rows) {
            grouped.computeIfAbsent(row.planId(), k -> new ArrayList<>())
                    .add(new CompiledPlanStep(
                            row.stepId(),
                            row.stepIrisCall(),
                            row.method(),
                            row.baseUrl(),
                            row.pathTemplate(),
                            transforms.get(row.transformRef()),
                            row.stopOnError(),
                            row.timeoutMs()
                    ));
        }
        grouped.values().forEach(list -> list.sort(Comparator.comparingInt(CompiledPlanStep::irisCall)));
        return grouped;
    }

    private Map<String, CompiledAggregator> compileAggregates(List<AggregateFieldRow> rows) {
        Map<String, List<CompiledField>> grouped = new LinkedHashMap<>();
        for (AggregateFieldRow row : rows) {
            grouped.computeIfAbsent(row.planId(), k -> new ArrayList<>())
                    .add(new CompiledField(row.targetField(), expressionParser.parse(row.sourceExpr()), row.optional()));
        }
        Map<String, CompiledAggregator> out = new LinkedHashMap<>();
        grouped.forEach((k, v) -> out.put(k, new CompiledAggregator(List.copyOf(v))));
        return out;
    }
}
