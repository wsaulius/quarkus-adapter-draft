package com.example.adapter.expression;
import com.example.adapter.domain.ExecutionContext;

public record PathParamExpression(String paramName) implements MappingExpression {
    @Override public String source() { return "$.path." + paramName; }
    @Override public String evaluate(ExecutionContext context) { return context.pathParams().get(paramName); }
}
