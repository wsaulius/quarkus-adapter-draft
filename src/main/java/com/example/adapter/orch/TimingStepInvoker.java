/**
 * Decorator that records step execution time.
 *
 * <p>This class measures wall-clock duration for each step invocation and logs it.
 * It is intended to remain independent from transport and orchestration logic.
 */
package com.example.adapter.orch;

import com.example.adapter.domain.CompiledPlanStep;
import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.domain.StepResult;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimingStepInvoker implements StepInvoker {
    private final StepInvoker delegate;

    @Override
    public StepResult invoke(ExecutionContext context, CompiledPlanStep step, String url, JsonNode requestBody) {
        long started = System.nanoTime();
        try {
            return delegate.invoke(context, step, url, requestBody);
        } finally {
            log.info("Step {} took {} ms", step.id(), (System.nanoTime() - started) / 1_000_000L);
        }
    }
}
