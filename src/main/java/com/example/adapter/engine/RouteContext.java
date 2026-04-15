package com.example.adapter.engine;

import com.fasterxml.jackson.databind.JsonNode;

public record RouteContext(
        String tenant,
        String environment,
        String fullPath,
        String httpMethod,
        JsonNode body
) {
    public String lookupBody(String key) {
        if (body == null || key == null || key.isBlank()) return null;
        JsonNode node = body.get(key);
        return node == null || node.isNull() ? null : node.asText();
    }
}
