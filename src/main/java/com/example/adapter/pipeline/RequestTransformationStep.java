package com.example.adapter.pipeline;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import com.example.adapter.strategy.TransformStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class RequestTransformationStep implements ProcessingStep<ExecutionContext> {
    @Inject Instance<TransformStrategy> strategies;

    @Override
    public ExecutionContext apply(ExecutionContext context) {
        String type = context.route().definition().transformType();
        TransformStrategy strategy = strategies.stream()
                .filter(s -> s.type().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing transform strategy: " + type));
        return context.withOutboundRequest(strategy.transform(context));
    }
}
