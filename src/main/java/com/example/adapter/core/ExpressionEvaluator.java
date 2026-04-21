package com.example.adapter.core;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.expression.ExpressionDef;
import com.fasterxml.jackson.databind.JsonNode;

public interface ExpressionEvaluator {
    JsonNode evaluate(ExpressionDef expression, ExecutionContext context);
}
