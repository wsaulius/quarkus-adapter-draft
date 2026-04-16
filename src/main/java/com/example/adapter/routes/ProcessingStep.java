package com.example.adapter.routes;
@FunctionalInterface
public interface ProcessingStep<T> {
    T apply(T context);
    default ProcessingStep<T> andThen(ProcessingStep<T> next) { return ctx -> next.apply(this.apply(ctx)); }
}
