/**
 * Application-facing orchestration service.
 *
 * <p>This service converts inbound request data into an execution context, runs the pipeline,
 * and maps the final context into a response object returned by the REST layer.
 */
package com.example.adapter.service;

import com.example.adapter.domain.*;
import com.example.adapter.pipeline.AdapterPipeline;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AdapterService {
    @Inject AdapterPipeline pipeline;
    public ExecutionResult execute(String tenant, String environment, String path, String method, JsonNode body) {
        ExecutionContext result = pipeline.execute(ExecutionContext.initial(new RouteKey(tenant, environment, method), path, body));
        return new ExecutionResult(result.route().route().targetSystem(), result.route().route().planId(), result.executionMode(), result.pathParams(), result.stepResults(), result.finalResponse());
    }
}
