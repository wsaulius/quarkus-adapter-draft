package com.example.adapter.domain;

public record ExcelRouteDefinition(
        boolean enabled,
        int priority,
        String tenant,
        String environment,
        String inputMethod,
        String inputPathTemplate,
        String targetSystem,
        String targetBaseUrl,
        String targetPathTemplate,
        String targetMethod,
        String transformRef,
        int timeoutMs,
        String versionTag
) {}
