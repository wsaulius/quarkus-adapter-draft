package com.example.adapter.core;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.expression.ExpressionDef;
import com.example.adapter.expression.ExpressionKind;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DefaultExpressionEvaluator implements ExpressionEvaluator {
    @Inject
    ObjectMapper mapper;

    @Override
    public JsonNode evaluate(ExpressionDef expression, ExecutionContext context) {
        return switch (expression.kind()) {
            case PATH -> {
                String v = context.pathParams().get(expression.value());
                yield v == null ? null : mapper.getNodeFactory().textNode(v);
            }
            case BODY -> context.body() == null ? null : context.body().get(expression.value());
            case STEP -> context.stepResults().get(expression.value());
            case LITERAL -> mapper.getNodeFactory().textNode(expression.value());
        };
    }
}
