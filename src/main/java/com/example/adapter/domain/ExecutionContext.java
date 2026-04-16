package com.example.adapter.domain;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public record ExecutionContext(
        RouteKey routeKey,
        String fullPath,
        JsonNode body,
        CompiledRoute route,
        Map<String, String> pathParams,
        JsonNode outboundRequest,
        String resolvedUrl,
        Integer downstreamStatus,
        JsonNode downstreamResponse,
        String executionMode
) {
    public static ExecutionContext initial(RouteKey routeKey, String fullPath, JsonNode body) {
        return new ExecutionContext(routeKey, fullPath, body, null, Map.of(), null, null, null, null, null);
    }
    public ExecutionContext withRoute(CompiledRoute route, Map<String, String> pathParams) {
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, outboundRequest, resolvedUrl, downstreamStatus, downstreamResponse, executionMode);
    }
    public ExecutionContext withOutboundRequest(JsonNode outboundRequest) {
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, outboundRequest, resolvedUrl, downstreamStatus, downstreamResponse, executionMode);
    }
    public ExecutionContext withResolvedUrl(String resolvedUrl) {
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, outboundRequest, resolvedUrl, downstreamStatus, downstreamResponse, executionMode);
    }
    public ExecutionContext withInvocationResult(String executionMode, Integer downstreamStatus, JsonNode downstreamResponse) {
        return new ExecutionContext(routeKey, fullPath, body, route, pathParams, outboundRequest, resolvedUrl, downstreamStatus, downstreamResponse, executionMode);
    }
}
