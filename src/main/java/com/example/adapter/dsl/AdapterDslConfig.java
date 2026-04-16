package com.example.adapter.dsl;

import io.smallrye.config.ConfigMapping;
import java.util.Map;

@ConfigMapping(prefix = "adapter-dsl")
public interface AdapterDslConfig {
    Map<String, TransformDef> transforms();
    interface TransformDef {
        String type();
        Map<String, String> fields();
    }
}
