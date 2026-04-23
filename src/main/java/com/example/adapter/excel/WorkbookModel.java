package com.example.adapter.excel;

import com.example.adapter.domain.AggregateFieldRow;
import com.example.adapter.domain.RouteRow;
import com.example.adapter.domain.StepRow;
import com.example.adapter.domain.TransformFieldRow;

import java.util.List;

public record WorkbookModel(List<RouteRow> routes, List<StepRow> steps, List<TransformFieldRow> transforms,
                            List<AggregateFieldRow> aggregates) {
}
