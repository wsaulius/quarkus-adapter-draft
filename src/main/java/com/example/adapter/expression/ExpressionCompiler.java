package com.example.adapter.expression;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Deprecated
@ApplicationScoped
public class ExpressionCompiler {
    @Inject
    ExpressionParser parser;

    public ExpressionDef compile(String sourceExpr) {
        return parser.parse(sourceExpr);
    }
}
