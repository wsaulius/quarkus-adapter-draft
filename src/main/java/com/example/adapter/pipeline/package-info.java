/**
 * Request-processing pipeline.
 *
 * <p>This package assembles the high-level request flow:
 * route selection, plan execution, and final aggregation.
 *
 * <p>The pipeline is intentionally thin. Most domain work is delegated to the core
 * execution layer so that steps remain composable and easy to understand.
 */
package com.example.adapter.pipeline;
