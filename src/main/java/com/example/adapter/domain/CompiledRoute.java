package com.example.adapter.domain;

import com.example.adapter.template.PathTemplate;

import java.util.Map;

public record CompiledRoute(
        ExcelRouteDefinition definition,
        PathTemplate inboundTemplate,
        PathTemplate outboundTemplate
) {
    public Map<String, String> extract(String path) {
        return inboundTemplate.match(path);
    }

    public String renderOutbound(Map<String, String> vars) {
        return outboundTemplate.render(vars);
    }
}
