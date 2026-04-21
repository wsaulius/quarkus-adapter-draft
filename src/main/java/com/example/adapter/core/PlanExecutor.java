package com.example.adapter.core;

import com.example.adapter.domain.CompiledPlan;
import com.example.adapter.domain.ExecutionContext;

public interface PlanExecutor {
    ExecutionContext execute(ExecutionContext context, CompiledPlan plan);
}
