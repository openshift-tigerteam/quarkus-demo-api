package com.redhat.demo.application;

import com.redhat.demo.Role;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.Objects;

@Path("/v1/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "application", description = "Application Operations")
public class ApplicationResource {

    private final ApplicationService applicationService;

    public ApplicationResource(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All Applications",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Application.class)
            )
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed(Role.APPLICATION_READ)
    public Response get() {
        return Response.ok(applicationService.findAll()).build();
    }

    @GET
    @Path("/{applicationId}")
    @APIResponse(
            responseCode = "200",
            description = "Get Application by applicationId",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Application.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Application does not exist for applicationId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed(Role.APPLICATION_READ)
    public Response getById(@Parameter(name = "applicationId", required = true) @PathParam("applicationId") Long applicationId) {
        return applicationService.findById(applicationId)
                .map(application -> Response.ok(application).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "Application Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Application.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Application",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Application already exists for applicationId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed(Role.APPLICATION_WRITE)
    public Response post(@NotNull @Valid Application application, @Context UriInfo uriInfo) {
        Application created = applicationService.create(application);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(created.applicationId())).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("/{applicationId}")
    @APIResponse(
            responseCode = "204",
            description = "Application updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Application.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Application",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Application object does not have applicationId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable applicationId does not match Application.applicationId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Application found for applicationId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed(Role.APPLICATION_WRITE)
    public Response put(@Parameter(name = "applicationId", required = true) @PathParam("applicationId") Long applicationId, @NotNull @Valid Application application) {
        if (!Objects.equals(applicationId, application.applicationId())) {
            throw new WebApplicationException("Path variable applicationId does not match Application.applicationId", Response.Status.BAD_REQUEST);
        }
        applicationService.update(application);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{applicationId}")
    @APIResponse(
            responseCode = "204",
            description = "Application deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "No Application found for applicationId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @RolesAllowed(Role.APPLICATION_WRITE)
    public Response delete(@Parameter(name = "applicationId", required = true) @PathParam("applicationId") Long applicationId) {
        if (applicationService.findById(applicationId).isEmpty()) {
            throw new WebApplicationException(String.format("No Application found for applicationId[%s]", applicationId), Response.Status.NOT_FOUND);
        }
        applicationService.delete(applicationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
