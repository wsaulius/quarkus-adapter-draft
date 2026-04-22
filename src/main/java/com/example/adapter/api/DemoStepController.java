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
    @Path("/step1/{country}/{shopId}")
    public Map<String, Object> step1(@PathParam("country") String country,
                                     @PathParam("shopId") String shopId) {
        LOG.infof("Demo step1 received country=%s shopId=%s", country, shopId);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("step", "step1");
        out.put("country", country);
        out.put("shopId", shopId);
        out.put("countryEntity", "COUNTRY::" + country);
        out.put("shopEntity", "SHOP::" + shopId);
        out.put("message", "step1 resolved path variables into internal entities");

        LOG.infof("Demo step1 returning countryEntity=%s shopEntity=%s",
                out.get("countryEntity"), out.get("shopEntity"));
        return out;
    }

    @GET
    @Path("/step2/{country}/{promoCode}")
    public Map<String, Object> step2(@PathParam("country") String country,
                                     @PathParam("promoCode") String promoCode) {
        LOG.infof("Demo step2 received country=%s promoCode=%s", country, promoCode);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("step", "step2");
        out.put("country", country);
        out.put("promoCode", promoCode);
        out.put("promoEntity", "PROMO::" + promoCode);
        out.put("discountLabel", "DISCOUNT_FOR_" + country);
        out.put("message", "step2 resolved promo data for aggregation");

        LOG.infof("Demo step2 returning promoEntity=%s discountLabel=%s",
                out.get("promoEntity"), out.get("discountLabel"));
        return out;
    }

    @GET
    @Path("/pricing/{country}")
    public Map<String, Object> pricing(@PathParam("country") String country) {
        LOG.infof("Demo pricing received country=%s", country);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("step", "pricing");
        out.put("country", country);
        out.put("currency", "EUR");
        out.put("priceList", "STANDARD-" + country);
        out.put("message", "pricing data resolved for country");

        LOG.infof("Demo pricing returning currency=%s priceList=%s",
                out.get("currency"), out.get("priceList"));
        return out;
    }
}
