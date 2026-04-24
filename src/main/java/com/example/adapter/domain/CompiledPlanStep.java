package com.example.adapter.domain;
public record CompiledPlanStep(String id, int irisCall, String method, String baseUrl, String pathTemplate, CompiledTransform transform, boolean stopOnError, int timeoutMs) {}
