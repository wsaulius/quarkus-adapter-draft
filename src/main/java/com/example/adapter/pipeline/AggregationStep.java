package com.example.adapter.pipeline;

import com.example.adapter.domain.*;
import com.example.adapter.expression.ExpressionKind;
import com.example.adapter.fp.ProcessingStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AggregationStep implements ProcessingStep<ExecutionContext> {
    @Inject ObjectMapper mapper;

    @Override
    public ExecutionContext apply(ExecutionContext context) {
        ObjectNode out = mapper.createObjectNode();
        for (CompiledField field : context.route().plan().aggregator().fields()) {
            JsonNode v = eval(field.expression(), context);
            if (v != null && !v.isNull()) out.set(field.targetField(), v);
        }
        String mode = context.executionMode() == null ? "mock" : context.executionMode();
        return context.withFinalResponse(out, mode);
    }

    private JsonNode eval(com.example.adapter.expression.ExpressionDef expr, ExecutionContext ctx) {
        return switch (expr.kind()) {
            case PATH -> {
                String v = ctx.pathParams().get(expr.value());
                yield v == null ? null : mapper.getNodeFactory().textNode(v);
            }
            case BODY -> ctx.body() == null ? null : ctx.body().get(expr.value());
            case STEP -> ctx.stepResults().get(expr.value());
            case LITERAL -> mapper.getNodeFactory().textNode(expr.value());
        };
    }
}
