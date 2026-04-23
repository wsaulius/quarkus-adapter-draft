/**
 * Internal demo endpoints used to visualize orchestration behavior.
 *
 * <p>These endpoints allow the same application to act as both orchestrator and sample
 * downstream systems. They make it easy to observe path-variable substitution, step
 * execution, and aggregation without depending on external services.
 */
package com.example.adapter.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/demo-api")
@Produces(MediaType.APPLICATION_JSON)
public class DemoStepController {
    private static final Logger LOG = Logger.getLogger(DemoStepController.class);

    @GET
    @Path("/step1/{model}/{colorId}")
    public Map<String, Object> step1(@PathParam("model") String model,
                                     @PathParam("colorId") String colorId) {
        LOG.infof("Demo step1 received model=%s colorId=%s", model, colorId);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("step", "step1");
        out.put("model", model);
        out.put("colorId", colorId);
        out.put("modelEntity", "MODEL::" + model);
        out.put("shopEntity", "SHOP::" + colorId);
        out.put("message", "step1 resolved path variables into internal entities");

        LOG.infof("Demo step1 returning modelEntity=%s shopEntity=%s",
                out.get("modelEntity"), out.get("shopEntity"));
        return out;
    }

    @GET
    @Path("/step2/{model}/{promoCode}")
    public Map<String, Object> step2(@PathParam("model") String model,
                                     @PathParam("promoCode") String promoCode) {
        LOG.infof("Demo step2 received model=%s promoCode=%s", model, promoCode);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("step", "step2");
        out.put("model", model);
        out.put("promoCode", promoCode);
        out.put("promoEntity", "PROMO::" + promoCode);
        out.put("discountLabel", "DISCOUNT_FOR_" + model);
        out.put("message", "step2 resolved promo data for aggregation");

        LOG.infof("Demo step2 returning promoEntity=%s discountLabel=%s",
                out.get("promoEntity"), out.get("discountLabel"));
        return out;
    }

    @GET
    @Path("/irisCallAccumulated/{model}")
    public Map<String, Object> irisCallAccumulated(@PathParam("model") String model) {
        LOG.infof("Demo irisCallAccumulated received model=%s", model);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("step", "irisCallAccumulated");
        out.put("model", model);
        out.put("currency", "EUR");
        out.put("priceList", "STANDARD-" + model);
        out.put("message", "irisCallAccumulated data resolved for model");

        LOG.infof("Demo irisCallAccumulated returning currency=%s priceList=%s",
                out.get("currency"), out.get("priceList"));
        return out;
    }
}
