package com.example.adapter.excel;

import com.example.adapter.domain.*;
import java.util.List;
public record WorkbookModel(List<RouteRow> routes, List<StepRow> steps, List<TransformFieldRow> transforms, List<AggregateFieldRow> aggregates) {}
