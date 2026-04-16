package com.example.adapter.expression;

import com.example.adapter.domain.ExecutionContext;
import com.fasterxml.jackson.databind.JsonNode;

public record BodyFieldExpression(String fieldName) implements MappingExpression {
    @Override
    public String source() {
        return "$." + fieldName;
    }

    @Override
    public String evaluate(ExecutionContext context) {
        JsonNode body = context.body();
        if (body == null) {
            return null;
        }
        JsonNode node = body.get(fieldName);
        return node == null || node.isNull() ? null : node.asText();
    }
}
