package com.example.adapter.health;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class AdapterLivenessCheck implements HealthCheck {
    @Override public HealthCheckResponse call() { return HealthCheckResponse.up("adapter-liveness"); }
}
