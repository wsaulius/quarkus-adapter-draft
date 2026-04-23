/**
 * Creates the invoker chain used by plan execution.
 *
 * <p>The factory centralizes decorator composition so that execution code does not need
 * to know how logging, timing, or future concerns are layered.
 */
package com.example.adapter.orch;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StepInvokerFactory {
    @Inject
    BaseStepInvoker base;

    public StepInvoker create() {
        return new TimingStepInvoker(new LoggingStepInvoker(base));
    }
}
