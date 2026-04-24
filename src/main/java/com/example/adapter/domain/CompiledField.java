package com.example.adapter.domain;
import com.example.adapter.expression.ExpressionDef;
public record CompiledField(String targetField, ExpressionDef expression, boolean optional) {}
