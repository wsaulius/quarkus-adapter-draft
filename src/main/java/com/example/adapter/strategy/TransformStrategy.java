package com.example.adapter.strategy;

import com.example.adapter.domain.ExecutionContext;
import com.fasterxml.jackson.databind.JsonNode;

public interface TransformStrategy {
    String type();
    JsonNode transform(ExecutionContext context);
}
