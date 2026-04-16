package com.example.adapter.expression;
import com.example.adapter.domain.ExecutionContext;
public interface MappingExpression {
    String source();
    String evaluate(ExecutionContext context);
}
