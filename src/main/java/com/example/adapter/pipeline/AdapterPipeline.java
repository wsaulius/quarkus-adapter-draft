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
