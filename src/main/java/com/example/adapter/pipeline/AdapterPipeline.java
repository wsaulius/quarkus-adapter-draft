/**
 * Composes the main request-processing flow.
 *
 * <p>The pipeline coordinates three high-level stages:
 * <ul>
 *   <li>Route selection</li>
 *   <li>Plan execution</li>
 *   <li>Response aggregation</li>
 * </ul>
 *
 * <p>Each stage is implemented as a small processing step. This keeps the orchestration flow
 * readable while delegating the detailed work to specialized components.
 */
package com.example.adapter.pipeline;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AdapterPipeline {
    private final ProcessingStep<ExecutionContext> pipeline;

    @Inject
    public AdapterPipeline(RouteSelectionStep routeSelectionStep, PlanExecutionStep planExecutionStep, AggregationStep aggregationStep) {
        this.pipeline = routeSelectionStep.andThen(planExecutionStep).andThen(aggregationStep);
    }

    public ExecutionContext execute(ExecutionContext context) { return pipeline.apply(context); }
}
