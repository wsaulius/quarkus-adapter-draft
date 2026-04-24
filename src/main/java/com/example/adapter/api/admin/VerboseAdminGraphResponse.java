package com.example.adapter.api.admin;

import com.example.adapter.excel.error.MappingLoadFailure;
import java.util.List;

/**
 * Verbose graph response containing detailed route and plan information.
 */
public record VerboseAdminGraphResponse(
        boolean loaded,
        int routeCount,
        int planCount,
        String source,
        String executionMode,
        MappingLoadFailure failure,
        List<GraphRouteSummary> routes,
        List<GraphPlanSummary> plans
) {
}
