package com.example.adapter.health;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.dsl.DslRegistry;
import com.example.adapter.engine.RouteRegistry;
import com.example.adapter.excel.error.MappingLoadFailure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class AdapterReadinessCheck implements HealthCheck {
    @Inject RouteRegistry registry;
    @Inject DslRegistry dslRegistry;
    @Inject AdapterConfig config;

    @Override
    public HealthCheckResponse call() {
        if (registry.loaded()) {
            return HealthCheckResponse.named("adapter-readiness")
                    .up()
                    .withData("routeCount", registry.size())
                    .withData("transformCount", dslRegistry.allTransforms().size())
                    .withData("source", registry.source())
                    .withData("executionMode", config.execution().mockEnabled() ? "mock" : "real")
                    .build();
        }
        MappingLoadFailure failure = registry.failure();
        if (failure != null) {
            return HealthCheckResponse.named("adapter-readiness")
                    .down()
                    .withData("code", failure.code())
                    .withData("reason", failure.message())
                    .withData("ref", failure.ref())
                    .build();
        }
        return HealthCheckResponse.named("adapter-readiness")
                .down()
                .withData("reason", "No routes loaded")
                .build();
    }
}
