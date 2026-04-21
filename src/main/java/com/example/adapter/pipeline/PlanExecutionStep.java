package com.example.adapter.pipeline;

import com.example.adapter.domain.*;
import com.example.adapter.expression.ExpressionKind;
import com.example.adapter.orch.*;
import com.example.adapter.template.TemplateRenderer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PlanExecutionStep implements com.example.adapter.fp.ProcessingStep<ExecutionContext> {
    @Inject StepInvokerFactory invokerFactory;
    @Inject ObjectMapper mapper;

    @Override
    public ExecutionContext apply(ExecutionContext context) {
        StepInvoker invoker = invokerFactory.create();
        ExecutionContext current = context;
        for (CompiledPlanStep step : context.route().plan().steps()) {
            ObjectNode request = mapper.createObjectNode();
            if (step.transform() != null) {
                for (CompiledField field : step.transform().fields()) {
                    JsonNode v = eval(field.expression(), current);
                    if (v != null && !v.isNull()) request.set(field.targetField(), v);
                }
            }
            String url = step.baseUrl() + TemplateRenderer.render(step.pathTemplate(), current.pathParams());
            StepResult result = invoker.invoke(current, step, url, request);
            current = current.withStepResult(step.id(), result.responseBody(), result.mode());
            if (step.stopOnError() && result.status() >= 400) break;
        }
        return current;
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
