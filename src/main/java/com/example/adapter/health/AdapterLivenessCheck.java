package com.example.adapter.health;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.*;
@Liveness
@ApplicationScoped
public class AdapterLivenessCheck implements HealthCheck {
    @Override public HealthCheckResponse call() { return HealthCheckResponse.up("adapter-liveness"); }
}
