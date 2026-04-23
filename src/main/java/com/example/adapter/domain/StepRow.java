package com.example.adapter.domain;

public record StepRow(boolean enabled, String planId, String stepId, int stepIrisCall, String method, String baseUrl,
                      String pathTemplate, String transformRef, boolean stopOnError, int timeoutMs) {
}
