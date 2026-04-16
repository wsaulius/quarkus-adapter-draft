package com.example.adapter.api;

import com.example.adapter.domain.ExecutionResult;
import com.example.adapter.service.AdapterService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/adapter")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MappingController {
    @Inject AdapterService service;

    @POST
    @Path("/{tenant}/{environment}/{resource: .+}")
    public ExecutionResult execute(@PathParam("tenant") String tenant,
                                   @PathParam("environment") String environment,
                                   @PathParam("resource") String resource,
                                   JsonNode body) {
        return service.execute(tenant, environment, "/" + resource, "POST", body);
    }

    @POST
    @Path("/ping")
    public JsonNode ping(JsonNode input) { return input; }
}
