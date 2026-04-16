package com.example.adapter.api;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.dsl.DslRegistry;
import com.example.adapter.engine.RouteRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
        return Map.of(
                "loaded", registry.loaded(),
                "count", registry.size(),
                "source", registry.source(),
                "executionMode", config.execution().mockEnabled() ? "mock" : "real",
                "transforms", dslRegistry.allTransforms().keySet(),
                "routes", registry.all().stream().map(r -> Map.of(
                        "tenant", r.definition().tenant(),
                        "environment", r.definition().environment(),
                        "inputMethod", r.definition().inputMethod(),
                        "inputPathTemplate", r.definition().inputPathTemplate(),
                        "targetSystem", r.definition().targetSystem(),
                        "targetPathTemplate", r.definition().targetPathTemplate(),
                        "priority", r.definition().priority(),
                        "transformRef", r.definition().transformRef(),
                        "transformType", r.transform().type()
                )).toList()
        );
    }
}
