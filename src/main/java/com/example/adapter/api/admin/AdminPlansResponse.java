package com.example.adapter.api.admin;

import com.example.adapter.excel.error.MappingLoadFailure;
import java.util.List;

/**
 * Typed response model for the admin plans endpoint.
 */
public record AdminPlansResponse(
        boolean loaded,
        int count,
        String source,
        String executionMode,
        MappingLoadFailure failure,
        List<PlanSummary> plans
) {
}
