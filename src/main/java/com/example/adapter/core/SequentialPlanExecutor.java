package com.example.adapter.core;

import com.example.adapter.domain.CompiledField;
import com.example.adapter.domain.CompiledPlan;
import com.example.adapter.domain.CompiledPlanStep;
import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.domain.StepResult;
import com.example.adapter.orch.StepInvoker;
import com.example.adapter.orch.StepInvokerFactory;
import com.example.adapter.template.TemplateRenderer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SequentialPlanExecutor implements PlanExecutor {
    @Inject
    StepInvokerFactory invokerFactory;

    @Inject
    ObjectMapper mapper;

    @Inject
    ExpressionEvaluator expressionEvaluator;

    @Override
    public ExecutionContext execute(ExecutionContext context, CompiledPlan plan) {
        StepInvoker invoker = invokerFactory.create();
        ExecutionContext current = context;

        for (CompiledPlanStep step : plan.steps()) {
            ObjectNode request = mapper.createObjectNode();
            if (step.transform() != null) {
                for (CompiledField field : step.transform().fields()) {
                    JsonNode value = expressionEvaluator.evaluate(field.expression(), current);
                    if (value != null && !value.isNull()) {
                        request.set(field.targetField(), value);
                    }
                }
            }

            String url = step.baseUrl() + TemplateRenderer.render(step.pathTemplate(), current.pathParams());
            StepResult result = invoker.invoke(current, step, url, request);
            current = current.withStepResult(step.id(), result.responseBody(), result.mode());

            if (step.stopOnError() && result.status() >= 400) {
                break;
            }
        }

        return current;
    }
}
