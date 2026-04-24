package com.example.adapter.api.admin;

/**
 * Summary view of one compiled route.
 */
public record RouteSummary(
        String tenant,
        String environment,
        String inputMethod,
        String inputPathTemplate,
        String planId,
        int stepCount
) {
}
