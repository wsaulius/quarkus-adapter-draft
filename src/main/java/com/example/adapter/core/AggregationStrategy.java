package com.example.adapter.core;

import com.example.adapter.domain.CompiledAggregator;
import com.example.adapter.domain.ExecutionContext;
import com.fasterxml.jackson.databind.JsonNode;

public interface AggregationStrategy {
    JsonNode aggregate(ExecutionContext context, CompiledAggregator aggregator);
}
