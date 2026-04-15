package com.example.adapter.domain;

import java.util.Map;

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
        String transformType,
        Map<String, String> requestMapping,
        int timeoutMs,
        String versionTag
) {}
