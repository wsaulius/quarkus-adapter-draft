/**
 * Workbook loading and compilation layer.
 *
 * <p>This package is responsible for turning Excel sheets into validated runtime objects.
 * It is intentionally separate from request execution.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Load workbook sheets</li>
 *   <li>Validate required structure and values</li>
 *   <li>Compile row-based configuration into an immutable orchestration graph</li>
 * </ul>
 *
 * <p>This is the compile-time side of the application. No downstream HTTP calls are made here.
 */
package com.example.adapter.excel;
