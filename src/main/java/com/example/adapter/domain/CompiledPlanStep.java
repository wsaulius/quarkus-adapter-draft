package com.example.adapter.domain;
public record CompiledPlanStep(String id, int order, String method, String baseUrl, String pathTemplate, CompiledTransform transform, boolean stopOnError, int timeoutMs) {}
