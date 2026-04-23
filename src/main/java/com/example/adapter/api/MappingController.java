/**
 * REST entry point for orchestrated requests.
 *
 * <p>This controller accepts inbound requests, delegates execution to the service layer,
 * and returns the aggregated orchestration result.
 *
 * <p>The controller is intentionally thin. Route selection and downstream orchestration
 * are handled by compiled configuration and the execution pipeline.
 */
package com.example.adapter.api;

import com.example.adapter.domain.ExecutionResult;
import com.example.adapter.service.AdapterService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/orchestrator")
@Produces(MediaType.APPLICATION_JSON)
public class MappingController {
    @Inject
    AdapterService service;

    @GET
    @Path("/{tenant}/{environment}/{resource: .+}")
    public ExecutionResult execute(@PathParam("tenant") String tenant,
                                   @PathParam("environment") String environment,
                                   @PathParam("resource") String resource) {
        return service.execute(tenant, environment, "/" + resource, "GET", null);
    }
}
