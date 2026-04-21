package com.example.adapter.orch;

import com.example.adapter.domain.*;
import com.fasterxml.jackson.databind.JsonNode;

public interface StepInvoker {
    StepResult invoke(ExecutionContext context, CompiledPlanStep step, String url, JsonNode requestBody);
}
