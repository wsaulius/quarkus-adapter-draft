package com.example.adapter.pipeline;

import com.example.adapter.core.AggregationStrategy;
import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AggregationStep implements ProcessingStep<ExecutionContext> {
    @Inject
    AggregationStrategy aggregationStrategy;

    @Override
    public ExecutionContext apply(ExecutionContext context) {
        String mode = context.executionMode() == null ? "mock" : context.executionMode();
        return context.withFinalResponse(
                aggregationStrategy.aggregate(context, context.route().plan().aggregator()),
                mode
        );
    }
}
