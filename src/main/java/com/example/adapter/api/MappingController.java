
package com.example.adapter.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.JsonNode;

@Path("/adapter")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MappingController {

    @POST
    @Path("/ping")
    public JsonNode ping(JsonNode input) {
        return input;
    }
}
