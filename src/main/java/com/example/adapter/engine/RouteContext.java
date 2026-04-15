package com.example.adapter.engine;

import com.fasterxml.jackson.databind.JsonNode;

public record RouteContext(String tenant, String environment, String resource, String httpMethod, JsonNode body) {
    public String lookup(String key) {
        if (body == null || key == null || key.isBlank()) return null;
        JsonNode node = body.get(key);
        return node == null || node.isNull() ? null : node.asText();
    }
}
