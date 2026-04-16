package com.example.adapter.pipeline;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import com.example.adapter.strategy.InvocationStrategy;
import com.example.adapter.strategy.MockInvocationStrategy;
import com.example.adapter.strategy.RealHttpInvocationStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InvocationStep implements ProcessingStep<ExecutionContext> {
    @Inject AdapterConfig config;
    @Inject MockInvocationStrategy mockStrategy;
    @Inject RealHttpInvocationStrategy realStrategy;
    @Override
    public ExecutionContext apply(ExecutionContext context) {
        InvocationStrategy strategy = config.execution().mockEnabled() ? mockStrategy : realStrategy;
        return strategy.invoke(context);
    }
}
