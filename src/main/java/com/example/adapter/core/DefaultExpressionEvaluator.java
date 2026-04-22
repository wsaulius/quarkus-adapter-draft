/**
 * Default runtime evaluator for parsed expressions.
 *
 * <p>This class interprets {@link com.example.adapter.expression.ExpressionDef}
 * instances against the current {@link com.example.adapter.domain.ExecutionContext}.
 *
 * <p>Supported sources:
 * <ul>
 *   <li>PATH: values extracted from the inbound route template</li>
 *   <li>BODY: values from the inbound request body</li>
 *   <li>STEP: values from previously executed step results</li>
 *   <li>LITERAL: constant values from workbook configuration</li>
 * </ul>
 *
 * <p>The evaluator is intentionally small and explicit. It is meant to be predictable,
 * easy to validate, and easy to extend without introducing a general scripting engine.
 */
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
