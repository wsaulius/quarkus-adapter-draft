package com.example.adapter.api.admin;

import java.util.List;

/**
 * Detailed plan view for the admin graph endpoint.
 */
public record GraphPlanSummary(
        String planId,
        int stepCount,
        List<GraphStepSummary> steps,
        List<String> aggregateFields
) {
}
