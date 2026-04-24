package com.example.adapter.api.admin;

import java.util.List;

/**
 * Detailed step view for the admin graph endpoint.
 */
public record GraphStepSummary(
        String stepId,
        int order,
        String method,
        String baseUrl,
        String pathTemplate,
        String transformRef,
        boolean stopOnError,
        int timeoutMs,
        List<String> transformFields
) {
}
