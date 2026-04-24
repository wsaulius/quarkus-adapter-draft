package com.example.adapter.domain;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
public record ExecutionResult(String matchedTargetSystem, String planId, String mode, Map<String,String> extractedPathParams, Map<String,JsonNode> stepResults, JsonNode finalResponse) {}
