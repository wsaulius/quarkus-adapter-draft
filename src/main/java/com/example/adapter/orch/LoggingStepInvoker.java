/**
 * Decorator that adds invocation logging around a {@link StepInvoker}.
 *
 * <p>This class logs the start and completion of each step call without changing
 * transport behavior. It demonstrates the Decorator pattern used in the invocation layer.
 */
package com.example.adapter.orch;

import com.example.adapter.domain.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggingStepInvoker implements StepInvoker {
    private final StepInvoker delegate;
    @Override
    public StepResult invoke(ExecutionContext context, CompiledPlanStep step, String url, JsonNode requestBody) {
        log.info("Invoking step {} -> {}", step.id(), url);
        StepResult result = delegate.invoke(context, step, url, requestBody);
        log.info("Completed step {} with status {}", step.id(), result.status());
        return result;
    }
}
