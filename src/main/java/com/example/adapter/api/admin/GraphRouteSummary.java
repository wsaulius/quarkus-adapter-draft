package com.example.adapter.api.admin;

/**
 * Detailed route view for the admin graph endpoint.
 */
public record GraphRouteSummary(
        String tenant,
        String environment,
        String inputMethod,
        String inputPathTemplate,
        String targetSystem,
        String planId,
        int stepCount
) {
}
