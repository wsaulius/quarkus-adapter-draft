package com.example.adapter.expression;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MappingExpressionCompiler {

    public List<FieldMapping> compile(Map<String, String> rawMapping) {
        List<FieldMapping> compiled = new ArrayList<>();
        rawMapping.forEach((targetField, sourceExpr) ->
                compiled.add(new FieldMapping(targetField, compileOne(sourceExpr))));
        return List.copyOf(compiled);
    }

    private MappingExpression compileOne(String sourceExpr) {
        if (sourceExpr == null) {
            return new LiteralExpression("");
        }
        if (sourceExpr.startsWith("$.path.")) {
            return new PathParamExpression(sourceExpr.substring("$.path.".length()));
        }
        if (sourceExpr.startsWith("$.")) {
            return new BodyFieldExpression(sourceExpr.substring(2));
        }
        return new LiteralExpression(sourceExpr);
    }
}
