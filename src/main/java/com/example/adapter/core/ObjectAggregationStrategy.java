/**
 * Aggregates step results into a JSON object.
 *
 * <p>This strategy evaluates compiled aggregate field definitions and writes the resulting
 * values into a single object node. In practice, it is the final assembly stage of the
 * orchestrator response.
 *
 * <p>The strategy is separate from plan execution so that other aggregation modes can be
 * introduced later without changing step execution itself.
 */
package com.example.adapter.core;

import com.example.adapter.domain.CompiledAggregator;
import com.example.adapter.domain.CompiledField;
import com.example.adapter.domain.ExecutionContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ObjectAggregationStrategy implements AggregationStrategy {
    private static final Logger LOG = Logger.getLogger(ObjectAggregationStrategy.class);

    @Inject
    ObjectMapper mapper;

    @Inject
    ExpressionEvaluator expressionEvaluator;

    @Override
    public JsonNode aggregate(ExecutionContext context, CompiledAggregator aggregator) {
        ObjectNode out = mapper.createObjectNode();
        for (CompiledField field : aggregator.fields()) {
            JsonNode value = expressionEvaluator.evaluate(field.expression(), context);
            if (value != null && !value.isNull()) {
                out.set(field.targetField(), value);
                LOG.infof("Aggregation mapped targetField=%s from expressionKind=%s expressionValue=%s",
                        field.targetField(),
                        field.expression().kind(),
                        field.expression().value());
            }
        }
        LOG.infof("Aggregation produced finalResponse=%s", out);
        return out;
    }
}
