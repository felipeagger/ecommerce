package org.felipeagger.ecommerce.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.felipeagger.ecommerce.entity.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthService {

    @POST
    @Path("/validator")
    Auth validateToken(String body);

}