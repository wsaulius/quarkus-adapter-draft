package com.example.adapter.health;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.engine.RouteRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class AdapterReadinessCheck implements HealthCheck {
    @Inject RouteRegistry registry;
    @Inject AdapterConfig config;

    @Override
    public HealthCheckResponse call() {
        if (registry.loaded()) {
            return HealthCheckResponse.named("adapter-readiness")
                    .up()
                    .withData("routeCount", registry.size())
                    .withData("source", registry.source())
                    .withData("executionMode", config.execution().mockEnabled() ? "mock" : "real")
                    .build();
        }
        return HealthCheckResponse.named("adapter-readiness")
                .down()
                .withData("reason", "No routes loaded")
                .build();
    }
}
