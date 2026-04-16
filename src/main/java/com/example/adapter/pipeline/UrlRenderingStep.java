package com.example.adapter.pipeline;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UrlRenderingStep implements ProcessingStep<ExecutionContext> {
    @Override
    public ExecutionContext apply(ExecutionContext context) {
        String url = context.route().definition().targetBaseUrl() +
                context.route().outboundRenderer().apply(context.pathParams());
        return context.withResolvedUrl(url);
    }
}
