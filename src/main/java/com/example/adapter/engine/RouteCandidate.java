package com.example.adapter.engine;

import com.example.adapter.domain.CompiledRoute;
import java.util.Map;

public record RouteCandidate(
        CompiledRoute route,
        Map<String, String> pathParams
) {}
