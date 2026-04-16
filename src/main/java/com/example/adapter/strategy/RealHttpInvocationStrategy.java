package com.example.adapter.strategy;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.domain.ExecutionContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class RealHttpInvocationStrategy implements InvocationStrategy {
    @Inject AdapterConfig config;
    @Inject ObjectMapper mapper;

    @Override
    public ExecutionContext invoke(ExecutionContext context) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(config.execution().connectTimeoutMs()))
                    .build();

            String payload = mapper.writeValueAsString(context.outboundRequest());
            HttpRequest.Builder b = HttpRequest.newBuilder()
                    .uri(URI.create(context.resolvedUrl()))
                    .timeout(Duration.ofMillis(Math.max(context.route().definition().timeoutMs(), config.execution().readTimeoutMs())))
                    .header("Content-Type", "application/json");

            String method = context.route().definition().targetMethod().toUpperCase();
            if ("POST".equals(method)) b.POST(HttpRequest.BodyPublishers.ofString(payload));
            else if ("PUT".equals(method)) b.PUT(HttpRequest.BodyPublishers.ofString(payload));
            else if ("PATCH".equals(method)) b.method("PATCH", HttpRequest.BodyPublishers.ofString(payload));
            else if ("GET".equals(method)) b.GET();
            else b.method(method, HttpRequest.BodyPublishers.ofString(payload));

            HttpResponse<String> r = client.send(b.build(), HttpResponse.BodyHandlers.ofString());

            JsonNode body;
            try { body = mapper.readTree(r.body()); }
            catch (Exception ex) { body = mapper.createObjectNode().put("rawBody", r.body()); }

            return context.withInvocationResult("real", r.statusCode(), body);
        } catch (Exception e) {
            throw new IllegalStateException("Downstream invocation failed for url=" + context.resolvedUrl(), e);
        }
    }
}
