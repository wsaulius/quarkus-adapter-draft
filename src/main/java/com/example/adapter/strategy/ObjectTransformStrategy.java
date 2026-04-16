package com.example.adapter.strategy;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.expression.FieldMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ObjectTransformStrategy implements TransformStrategy {
    @Inject ObjectMapper mapper;
    @Override public String type() { return "object"; }

    @Override
    public JsonNode transform(ExecutionContext context) {
        ObjectNode out = mapper.createObjectNode();
        for (FieldMapping mapping : context.route().transform().fieldMappings()) {
            String value = mapping.expression().evaluate(context);
            if (value != null) out.put(mapping.targetField(), value);
        }
        return out;
    }
}
