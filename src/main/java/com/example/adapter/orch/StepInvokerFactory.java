package com.example.adapter.orch;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StepInvokerFactory {
    @Inject BaseStepInvoker base;
    public StepInvoker create() { return new TimingStepInvoker(new LoggingStepInvoker(base)); }
}
