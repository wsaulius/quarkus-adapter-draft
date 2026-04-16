package com.example.adapter.domain;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public record ExecutionResult(
        String matchedTargetSystem,
        String transformRef,
        String resolvedUrl,
        String mode,
        int downstreamStatus,
        Map<String, String> extractedPathParams,
        JsonNode outboundRequest,
        JsonNode downstreamResponse
) {}
