package com.example.adapter.strategy;
import com.example.adapter.domain.ExecutionContext;
public interface InvocationStrategy {
    ExecutionContext invoke(ExecutionContext context);
}
