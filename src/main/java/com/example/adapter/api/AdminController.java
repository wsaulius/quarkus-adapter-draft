package com.example.adapter.api;

import com.example.adapter.api.admin.AdminPlansResponse;
import com.example.adapter.api.admin.AdminResponseMapper;
import com.example.adapter.api.admin.AdminRoutesResponse;
import com.example.adapter.api.admin.VerboseAdminGraphResponse;
import com.example.adapter.config.AdapterConfig;
import com.example.adapter.engine.RouteRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Administrative endpoints for inspecting compiled orchestration state.
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminController {

    @Inject
    RouteRegistry registry;

    @Inject
    AdapterConfig config;

    @Inject
    AdminResponseMapper mapper;

    @GET
    @Path("/routes")
    public AdminRoutesResponse routes() {
        return mapper.toRoutesResponse(registry, config);
    }

    @GET
    @Path("/plans")
    public AdminPlansResponse plans() {
        return mapper.toPlansResponse(registry, config);
    }

    @GET
    @Path("/graph")
    public VerboseAdminGraphResponse graph() {
        return mapper.toGraphResponse(registry, config);
    }
}
