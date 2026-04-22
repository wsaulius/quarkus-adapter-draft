/**
 * Core runtime abstractions for the orchestrator.
 *
 * <p>This package contains the main execution-time contracts and default implementations
 * that sit between compiled workbook configuration and HTTP step invocation.
 *
 * <p>Design intent:
 * <ul>
 *   <li>Keep orchestration behavior explicit and replaceable</li>
 *   <li>Separate parse/compile concerns from runtime execution concerns</li>
 *   <li>Provide narrow interfaces for plan execution, expression evaluation, and aggregation</li>
 * </ul>
 *
 * <p>Main patterns used here:
 * <ul>
 *   <li>Strategy for plan execution and aggregation</li>
 *   <li>Functional core style for evaluation and aggregation logic</li>
 *   <li>Immutable graph model consumed at runtime</li>
 * </ul>
 */
package com.example.adapter.core;
