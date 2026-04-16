package com.example.adapter.dsl;

import com.example.adapter.domain.CompiledTransform;
import com.example.adapter.expression.MappingExpressionCompiler;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.LinkedHashMap;
import java.util.Map;

@Startup
@ApplicationScoped
public class DslCompiler {
    @Inject AdapterDslConfig config;
    @Inject MappingExpressionCompiler expressionCompiler;
    @Inject DslRegistry registry;

    @PostConstruct
    void init() {
        Map<String, CompiledTransform> compiled = new LinkedHashMap<>();
        config.transforms().forEach((name, def) -> {
            compiled.put(name, new CompiledTransform(
                    name,
                    def.type(),
                    expressionCompiler.compile(def.fields())
            ));
        });
        registry.replaceAll(compiled);
    }
}
