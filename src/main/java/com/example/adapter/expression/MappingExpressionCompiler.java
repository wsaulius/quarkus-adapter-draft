package com.example.adapter.expression;

import com.example.adapter.dsl.AdapterDslConfig;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MappingExpressionCompiler {
    public List<FieldMapping> compile(Map<String, AdapterDslConfig.FieldDef> rawMapping) {
        List<FieldMapping> compiled = new ArrayList<>();
        rawMapping.forEach((targetField, fieldDef) -> compiled.add(
                new FieldMapping(targetField, compileOne(fieldDef.source()), fieldDef.optional())
        ));
        return List.copyOf(compiled);
    }

    private MappingExpression compileOne(String sourceExpr) {
        if (sourceExpr == null) return new LiteralExpression("");
        if (sourceExpr.startsWith("$.path.")) return new PathParamExpression(sourceExpr.substring("$.path.".length()));
        if (sourceExpr.startsWith("$.")) return new BodyFieldExpression(sourceExpr.substring(2));
        return new LiteralExpression(sourceExpr);
    }
}
