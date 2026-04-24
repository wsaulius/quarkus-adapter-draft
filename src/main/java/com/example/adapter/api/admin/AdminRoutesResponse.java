package com.example.adapter.api.admin;

import com.example.adapter.excel.error.MappingLoadFailure;
import java.util.List;

/**
 * Typed response model for the admin routes endpoint.
 */
public record AdminRoutesResponse(
        boolean loaded,
        int count,
        String source,
        String executionMode,
        MappingLoadFailure failure,
        List<RouteSummary> routes
) {
}
