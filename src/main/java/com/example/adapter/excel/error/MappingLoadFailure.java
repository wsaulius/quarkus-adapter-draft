package com.example.adapter.excel.error;

public record MappingLoadFailure(
        String code,
        String message,
        String ref
) {}
