package com.example.adapter.api;

import com.example.adapter.config.AdapterConfig;
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
    @Inject
    RouteRegistry registry;
    @Inject
    AdapterConfig config;

    @GET
    @Path("/routes")
    public Map<String, Object> routes() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("loaded", registry.loaded());
        out.put("count", registry.size());
        out.put("source", registry.source());
        out.put("executionMode", config.execution().mockEnabled() ? "mock" : "real");
        out.put("failure", registry.failure());
        out.put("routes", registry.all().stream().map(r -> Map.of(
                "tenant", r.route().tenant(),
                "environment", r.route().environment(),
                "inputMethod", r.route().inputMethod(),
                "inputPathTemplate", r.route().inputPathTemplate(),
                "planId", r.route().planId(),
                "stepCount", r.plan().steps().size()
        )).toList());
        return out;
    }
}
