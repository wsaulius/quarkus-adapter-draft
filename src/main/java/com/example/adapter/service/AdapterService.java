package com.example.adapter.service;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.domain.ExecutionResult;
import com.example.adapter.domain.RouteKey;
import com.example.adapter.pipeline.AdapterPipeline;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AdapterService {
    @Inject AdapterPipeline pipeline;

    public ExecutionResult execute(String tenant, String environment, String path, String method, JsonNode body) {
        ExecutionContext initial = ExecutionContext.initial(new RouteKey(tenant, environment, method), path, body);
        ExecutionContext result = pipeline.execute(initial);
        return new ExecutionResult(
                result.route().definition().targetSystem(),
                result.route().definition().transformRef(),
                result.resolvedUrl(),
                result.executionMode(),
                result.downstreamStatus(),
                result.pathParams(),
                result.outboundRequest(),
                result.downstreamResponse()
        );
    }
}
