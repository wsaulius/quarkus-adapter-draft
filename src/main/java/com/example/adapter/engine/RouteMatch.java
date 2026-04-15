package com.example.adapter.engine;

import com.example.adapter.domain.CompiledRoute;

import java.util.Map;

public record RouteMatch(
        CompiledRoute route,
        Map<String, String> pathParams
) {}
