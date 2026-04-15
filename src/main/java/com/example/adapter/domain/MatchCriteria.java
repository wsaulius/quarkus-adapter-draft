package com.example.adapter.domain;
public record MatchCriteria(String tenant, String environment, String resource, String inputKey, String inputValue, String httpMethod) {}
