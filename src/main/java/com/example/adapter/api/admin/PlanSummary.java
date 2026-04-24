package com.example.adapter.api.admin;

/**
 * Summary view of one compiled plan.
 */
public record PlanSummary(
        String planId,
        int stepCount,
        java.util.List<String> stepIds,
        java.util.List<String> aggregateFields
) {
}
