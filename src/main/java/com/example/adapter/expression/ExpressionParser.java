package com.example.adapter.expression;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpressionParser {
    public ExpressionDef parse(String sourceExpr) {
        if (sourceExpr == null) {
            return new ExpressionDef(ExpressionKind.LITERAL, "");
        }
        if (sourceExpr.startsWith("$.steps.")) {
            return new ExpressionDef(ExpressionKind.STEP, sourceExpr.substring("$.steps.".length()));
        }
        if (sourceExpr.startsWith("$.path.")) {
            return new ExpressionDef(ExpressionKind.PATH, sourceExpr.substring("$.path.".length()));
        }
        if (sourceExpr.startsWith("$.")) {
            return new ExpressionDef(ExpressionKind.BODY, sourceExpr.substring(2));
        }
        return new ExpressionDef(ExpressionKind.LITERAL, sourceExpr);
    }
}
