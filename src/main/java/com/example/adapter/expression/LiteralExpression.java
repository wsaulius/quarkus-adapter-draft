package com.example.adapter.expression;
import com.example.adapter.domain.ExecutionContext;

public record LiteralExpression(String literalValue) implements MappingExpression {
    @Override public String source() { return literalValue; }
    @Override public String evaluate(ExecutionContext context) { return literalValue; }
}
