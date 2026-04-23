package com.example.adapter.domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public record ExecutionContext(RouteKey routeKey, String fullPath, JsonNode body, CompiledRoute route,
                               Map<String, String> pathParams, Map<String, JsonNode> stepResults,
                               JsonNode finalResponse, String executionMode) {
    public static ExecutionContext initial(RouteKey key, String path, JsonNode body) {
        return new ExecutionContext(key, path, body, null, Map.of(), new LinkedHashMap<>(), null, null);
    }

    public ExecutionContext withRoute(CompiledRoute route, Map<String, String> pathParams) {
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, new LinkedHashMap<>(stepResults), finalResponse, executionMode);
    }

    public ExecutionContext withStepResult(String id, JsonNode response, String mode) {
        Map<String, JsonNode> m = new LinkedHashMap<>(stepResults);
        m.put(id, response);
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, m, finalResponse, mode);
    }

    public ExecutionContext withFinalResponse(JsonNode response, String mode) {
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, new LinkedHashMap<>(stepResults), response, mode);
    }
}
