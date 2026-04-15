package com.example.adapter.engine;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.domain.ExecutionResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class MappingEngine {
    @Inject RouteRegistry registry;
    @Inject ObjectMapper mapper;
    @Inject AdapterConfig config;

    public ExecutionResult execute(RouteContext context) {
        RouteMatch match = registry.find(context).orElseThrow(() -> new IllegalArgumentException("No mapping found"));
        var route = match.route();
        var pathParams = match.pathParams();

        JsonNode outbound = transform(context, pathParams, route.definition().requestMapping());
        String resolvedUrl = route.definition().targetBaseUrl() + route.renderOutbound(pathParams);

        int status;
        JsonNode response;
        String mode = config.execution().mockEnabled() ? "mock" : "real";

        if (config.execution().mockEnabled()) {
            ObjectNode mock = mapper.createObjectNode();
            mock.put("status", "MOCK_OK");
            mock.put("method", route.definition().targetMethod());
            mock.put("url", resolvedUrl);
            mock.set("echo", outbound);
            status = 200;
            response = mock;
        } else {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofMillis(config.execution().connectTimeoutMs()))
                        .build();
                String payload = mapper.writeValueAsString(outbound);
                HttpRequest.Builder b = HttpRequest.newBuilder()
                        .uri(URI.create(resolvedUrl))
                        .timeout(Duration.ofMillis(Math.max(route.definition().timeoutMs(), config.execution().readTimeoutMs())))
                        .header("Content-Type", "application/json");
                String method = route.definition().targetMethod().toUpperCase();
                if ("POST".equals(method)) b.POST(HttpRequest.BodyPublishers.ofString(payload));
                else if ("PUT".equals(method)) b.PUT(HttpRequest.BodyPublishers.ofString(payload));
                else if ("PATCH".equals(method)) b.method("PATCH", HttpRequest.BodyPublishers.ofString(payload));
                else if ("GET".equals(method)) b.GET();
                else b.method(method, HttpRequest.BodyPublishers.ofString(payload));

                HttpResponse<String> r = client.send(b.build(), HttpResponse.BodyHandlers.ofString());
                status = r.statusCode();
                try { response = mapper.readTree(r.body()); }
                catch (Exception ex) { response = mapper.createObjectNode().put("rawBody", r.body()); }
            } catch (Exception e) {
                throw new IllegalStateException("Downstream invocation failed for url=" + resolvedUrl, e);
            }
        }

        return new ExecutionResult(
                route.definition().targetSystem(),
                resolvedUrl,
                mode,
                status,
                pathParams,
                outbound,
                response
        );
    }

    private JsonNode transform(RouteContext context, java.util.Map<String, String> pathParams, java.util.Map<String, String> mapping) {
        ObjectNode out = mapper.createObjectNode();
        mapping.forEach((targetField, sourceExpr) -> {
            if (sourceExpr.startsWith("$.path.")) {
                String key = sourceExpr.substring("$.path.".length());
                String value = pathParams.get(key);
                if (value != null) out.put(targetField, value);
            } else if (sourceExpr.startsWith("$.")) {
                String key = sourceExpr.substring(2);
                String value = context.lookupBody(key);
                if (value != null) out.put(targetField, value);
            }
        });
        return out;
    }
}
