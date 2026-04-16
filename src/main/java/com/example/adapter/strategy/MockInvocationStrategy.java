package com.example.adapter.strategy;

import com.example.adapter.domain.ExecutionContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MockInvocationStrategy implements InvocationStrategy {
    @Inject ObjectMapper mapper;
    @Override
    public ExecutionContext invoke(ExecutionContext context) {
        ObjectNode response = mapper.createObjectNode();
        response.put("status", "MOCK_OK");
        response.put("method", context.route().definition().targetMethod());
        response.put("url", context.resolvedUrl());
        response.set("echo", context.outboundRequest());
        return context.withInvocationResult("mock", 200, response);
    }
}
