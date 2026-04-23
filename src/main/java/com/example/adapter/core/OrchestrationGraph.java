/**
 * Immutable root object for compiled orchestration state.
 *
 * <p>This graph is the runtime product of workbook compilation. It contains the routes
 * available to the application and serves as the single object stored in the registry.
 *
 * <p>The design goal is to keep runtime state simple: requests read from one compiled graph
 * instead of interacting with several unrelated maps or builders.
 */
package com.example.adapter.core;

import com.example.adapter.domain.CompiledRoute;
import java.util.List;

public record OrchestrationGraph(List<CompiledRoute> routes) {}
