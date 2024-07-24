package com.redhat.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.net.URI;

@Path("/")
public class IndexResource {

    private static final URI uri = URI.create("/swagger-ui");

    @GET
    @Operation(hidden = true)
    public Response get() {
       return Response.temporaryRedirect(uri).build();
    }

}