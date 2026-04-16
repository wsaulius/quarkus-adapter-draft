package com.example.adapter.domain;
import com.example.adapter.expression.FieldMapping;
import java.util.List;
public record CompiledTransform(String name, String type, List<FieldMapping> fieldMappings) {}
