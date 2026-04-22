/**
 * Performs the actual step transport call.
 *
 * <p>This class is the transport-level invoker used by orchestration steps. It supports:
 * <ul>
 *   <li>Mock mode for local testing and deterministic demos</li>
 *   <li>Real HTTP execution using the JDK HTTP client</li>
 * </ul>
 *
 * <p>It is intentionally minimal and is normally wrapped by decorators that add logging,
 * timing, and other cross-cutting behavior.
 */
package com.example.adapter.orch;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.domain.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

@ApplicationScoped
public class BaseStepInvoker implements StepInvoker {
    @Inject AdapterConfig config;
    @Inject ObjectMapper mapper;

    @Override
    public StepResult invoke(ExecutionContext context, CompiledPlanStep step, String url, JsonNode requestBody) {
        try {
            if (config.execution().mockEnabled()) {
                JsonNode response = mapper.createObjectNode()
                        .put("status", "MOCK_OK")
                        .put("stepId", step.id())
                        .put("url", url)
                        .set("request", requestBody);
                return new StepResult(step.id(), url, 200, requestBody, response, "mock");
            }
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(config.execution().connectTimeoutMs())).build();
            HttpRequest.Builder b = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofMillis(step.timeoutMs()));
            if ("GET".equalsIgnoreCase(step.method())) b.GET();
            else b.method(step.method().toUpperCase(), HttpRequest.BodyPublishers.ofString(requestBody == null ? "" : mapper.writeValueAsString(requestBody)));
            HttpResponse<String> r = client.send(b.build(), HttpResponse.BodyHandlers.ofString());
            JsonNode body;
            try { body = mapper.readTree(r.body()); } catch (Exception ex) { body = mapper.createObjectNode().put("rawBody", r.body()); }
            return new StepResult(step.id(), url, r.statusCode(), requestBody, body, "real");
        } catch (Exception e) {
            throw new IllegalStateException("Step invocation failed for " + step.id() + " at " + url, e);
        }
    }
}
