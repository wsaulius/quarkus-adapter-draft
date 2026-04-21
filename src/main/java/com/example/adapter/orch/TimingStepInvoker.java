package com.example.adapter.orch;

import com.example.adapter.domain.*;
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
        try { return delegate.invoke(context, step, url, requestBody); }
        finally { log.info("Step {} took {} ms", step.id(), (System.nanoTime() - started) / 1_000_000L); }
    }
}
