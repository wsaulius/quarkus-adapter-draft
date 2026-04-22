/**
 * Sequential implementation of {@link PlanExecutor}.
 *
 * <p>This executor runs all steps in a {@link com.example.adapter.domain.CompiledPlan}
 * in declared order and updates the {@link com.example.adapter.domain.ExecutionContext}
 * after each call.
 *
 * <p>Per step it:
 * <ul>
 *   <li>Builds a request object from the compiled transform</li>
 *   <li>Resolves expressions against the current execution context</li>
 *   <li>Renders the configured URL template</li>
 *   <li>Invokes the downstream endpoint through the decorated invoker chain</li>
 *   <li>Stores the step response under the step id</li>
 * </ul>
 *
 * <p>This class is the main runtime realization of the one-to-many orchestration model.
 * A future parallel executor can implement the same interface without changing the pipeline.
 */
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
