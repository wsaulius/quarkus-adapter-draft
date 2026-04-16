package com.example.adapter.dsl;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import java.util.Map;

@ConfigMapping(prefix = "adapter-dsl")
public interface AdapterDslConfig {
    Map<String, TransformDef> transforms();

    interface TransformDef {
        String type();
        Map<String, FieldDef> fields();
    }

    interface FieldDef {
        String source();
        @WithDefault("false")
        boolean optional();
    }
}
