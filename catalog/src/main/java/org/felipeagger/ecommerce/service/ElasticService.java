package org.felipeagger.ecommerce.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/catalog")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ElasticService {

    @GET
    @Path("/item/_search")
    Object getCatalog();

    @POST
    @Path("/item/_search")
    Object getCatalogByQuery(String body);

}