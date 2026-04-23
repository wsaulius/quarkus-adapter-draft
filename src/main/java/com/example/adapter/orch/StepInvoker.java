package com.example.adapter.orch;

import com.example.adapter.domain.CompiledPlanStep;
import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.domain.StepResult;
import com.fasterxml.jackson.databind.JsonNode;

public interface StepInvoker {
    StepResult invoke(ExecutionContext context, CompiledPlanStep step, String url, JsonNode requestBody);
}
