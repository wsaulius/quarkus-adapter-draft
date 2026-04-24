package com.example.adapter.domain;
public record RouteRow(boolean enabled, int priority, String tenant, String environment, String inputMethod, String inputPathTemplate, String targetSystem, String planId) {}
