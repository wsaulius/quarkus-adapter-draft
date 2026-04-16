package com.example.adapter.pipeline;

import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AdapterPipeline {
    private final ProcessingStep<ExecutionContext> pipeline;

    @Inject
    public AdapterPipeline(RouteSelectionStep select,
                           RequestTransformationStep transform,
                           UrlRenderingStep render,
                           InvocationStep invoke) {
        this.pipeline = select.andThen(transform).andThen(render).andThen(invoke);
    }

    public ExecutionContext execute(ExecutionContext context) {
        return pipeline.apply(context);
    }
}
