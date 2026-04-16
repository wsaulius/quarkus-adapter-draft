package com.example.adapter.dsl;

import com.example.adapter.domain.CompiledTransform;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class DslRegistry {
    private final AtomicReference<Map<String, CompiledTransform>> transformsRef = new AtomicReference<>(Map.of());

    public void replaceAll(Map<String, CompiledTransform> transforms) {
        transformsRef.set(Map.copyOf(transforms));
    }

    public CompiledTransform transform(String name) {
        CompiledTransform t = transformsRef.get().get(name);
        if (t == null) {
            throw new IllegalArgumentException("Unknown transform_ref: " + name + ". Available transforms: " + transformsRef.get().keySet());
        }
        return t;
    }

    public Map<String, CompiledTransform> allTransforms() {
        return transformsRef.get();
    }
}
