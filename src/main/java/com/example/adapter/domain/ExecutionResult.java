package com.example.adapter.domain;
import com.fasterxml.jackson.databind.JsonNode;
public record ExecutionResult(String matchedTargetSystem, String resolvedUrl, String mode, int downstreamStatus, JsonNode outboundRequest, JsonNode downstreamResponse) {}
