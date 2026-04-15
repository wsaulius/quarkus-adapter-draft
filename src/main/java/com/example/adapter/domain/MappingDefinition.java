package com.example.adapter.domain;
import java.util.Map;
public record MappingDefinition(boolean enabled, int priority, String targetSystem, String targetBaseUrl, String targetPath, String httpMethod, String transformType, Map<String, String> requestMapping, int timeoutMs, String versionTag) {}
