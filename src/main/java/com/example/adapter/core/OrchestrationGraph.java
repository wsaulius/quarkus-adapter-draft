package com.example.adapter.core;

import com.example.adapter.domain.CompiledRoute;
import java.util.List;

public record OrchestrationGraph(List<CompiledRoute> routes) {}
