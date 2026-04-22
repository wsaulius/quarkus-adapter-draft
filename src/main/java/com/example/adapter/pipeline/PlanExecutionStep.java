/**
 * Delegates execution of the selected plan.
 *
 * <p>This step keeps the pipeline thin by forwarding execution to the configured
 * {@link com.example.adapter.core.PlanExecutor}. The concrete execution strategy
 * decides how plan steps are run.
 */
package com.example.adapter.pipeline;

import com.example.adapter.core.PlanExecutor;
import com.example.adapter.domain.ExecutionContext;
import com.example.adapter.fp.ProcessingStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PlanExecutionStep implements ProcessingStep<ExecutionContext> {
    @Inject
    PlanExecutor planExecutor;

    @Override
    public ExecutionContext apply(ExecutionContext context) {
        return planExecutor.execute(context, context.route().plan());
    }
}
