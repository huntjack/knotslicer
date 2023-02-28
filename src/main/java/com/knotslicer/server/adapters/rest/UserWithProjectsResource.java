package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.services.UserWithChildrenService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/users/{userId}/projects")
@RequestScoped
public class UserWithProjectsResource implements UserWithChildrenResource {
    private LinkReceiver linkReceiver;
    private UserWithChildrenService userWithChildrenService;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("userId")Long userId, @Context UriInfo uriInfo) {

        return null;
    }
    public UserWithProjectsResource(UserWithChildrenService userWithChildrenService,
                                    LinkReceiver linkReceiver){
        this.userWithChildrenService = userWithChildrenService;
        this.linkReceiver = linkReceiver;
    }
    protected UserWithProjectsResource() {}
}
