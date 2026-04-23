package com.example.adapter.domain;

public record CompiledPlan(String id, java.util.List<CompiledPlanStep> steps, CompiledAggregator aggregator) {
}
