/**
 * Domain model for compiled orchestration.
 *
 * <p>This package contains immutable records representing both workbook rows and the
 * compiled runtime graph. The runtime engine consumes the compiled types only.
 *
 * <p>Design rule:
 * raw workbook rows are short-lived, compiled objects are stable runtime artifacts.
 */
package com.example.adapter.domain;
