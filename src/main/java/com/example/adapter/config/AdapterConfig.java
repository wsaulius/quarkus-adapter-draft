package com.example.adapter.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "adapter")
public interface AdapterConfig {
    Excel excel();
    Execution execution();

    interface Excel {
        String source();
        @WithDefault("routes") String routesSheet();
        @WithDefault("steps") String stepsSheet();
        @WithDefault("transforms") String transformsSheet();
        @WithDefault("aggregates") String aggregatesSheet();
    }

    interface Execution {
        @WithDefault("true") boolean mockEnabled();
        @WithDefault("3000") int connectTimeoutMs();
        @WithDefault("5000") int readTimeoutMs();
    }
}
