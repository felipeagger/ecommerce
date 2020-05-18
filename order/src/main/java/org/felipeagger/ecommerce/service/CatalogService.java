package org.felipeagger.ecommerce.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.felipeagger.ecommerce.entity.Catalog;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CatalogService {

    @GET
    @Path("/getItem/{id}")
    Catalog getItem(@PathParam String id);

}