package com.example.adapter.api;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.dsl.DslRegistry;
import com.example.adapter.engine.RouteRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminController {
    @Inject RouteRegistry registry;
    @Inject DslRegistry dslRegistry;
    @Inject AdapterConfig config;

    @GET
    @Path("/routes")
    public Map<String, Object> routes() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("loaded", registry.loaded());
        out.put("count", registry.size());
        out.put("source", registry.source());
        out.put("executionMode", config.execution().mockEnabled() ? "mock" : "real");
        out.put("failure", registry.failure());

        out.put("transforms", dslRegistry.allTransforms().entrySet().stream().collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                e -> Map.of(
                        "type", e.getValue().type(),
                        "fields", e.getValue().fieldMappings().stream().map(f -> Map.of(
                                "target", f.targetField(),
                                "source", f.expression().source(),
                                "optional", f.optional()
                        )).toList()
                )
        )));
        out.put("routes", registry.all().stream().map(r -> Map.of(
                "tenant", r.definition().tenant(),
                "environment", r.definition().environment(),
                "inputMethod", r.definition().inputMethod(),
                "inputPathTemplate", r.definition().inputPathTemplate(),
                "targetSystem", r.definition().targetSystem(),
                "targetPathTemplate", r.definition().targetPathTemplate(),
                "priority", r.definition().priority(),
                "transformRef", r.definition().transformRef(),
                "transformType", r.transform().type()
        )).toList());
        return out;
    }
}
