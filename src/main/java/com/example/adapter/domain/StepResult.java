package com.example.adapter.domain;
import com.fasterxml.jackson.databind.JsonNode;
public record StepResult(String stepId, String url, int status, JsonNode requestBody, JsonNode responseBody, String mode) {}
