package org.felipeagger.ecommerce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.felipeagger.ecommerce.controller.CatalogController;
import org.felipeagger.ecommerce.entity.Cart;
import org.felipeagger.ecommerce.entity.Catalog;
import org.felipeagger.ecommerce.service.ElasticQuery;
import org.felipeagger.ecommerce.service.ElasticService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/")
@Consumes(MediaType.TEXT_HTML)                         
@Produces(MediaType.TEXT_HTML)
public class CatalogResource {

    @Inject
    private CatalogController catalogController;

    @Inject
    @RestClient
    ElasticService elasticService;

    @Inject @Channel("cart-topic") Emitter<String> cartAddEmitter;

    @Inject Template catalog;
    @Inject Template cartAdded;
    @Inject Template redirect;
    
    @GET                                                   
    public TemplateInstance getCatalog(@QueryParam Optional<Integer> size) {

        List<Catalog> catalogItems = catalogController.parse(elasticService.getCatalog());

        return catalog.data("catalog", catalogItems);

    }

    @GET                                                   
    @Path("catalog")
    public TemplateInstance getCatalog(@QueryParam("search") String search, @QueryParam Optional<Integer> size) {

        Object objSearch;

        if(!search.isEmpty()){
            objSearch = elasticService.getCatalogByQuery(ElasticQuery.getQuery(search));
        } else {
            objSearch = elasticService.getCatalog();
        }

        List<Catalog> catalogItems = catalogController.parse(objSearch);
       

        return catalog.data("catalog", catalogItems);

    }

    @GET
    @Path("/{id}/cart")
    public TemplateInstance setToCart(@HeaderParam("Cookie") String cookie, @PathParam("id") String id) {

        if(!catalogController.validateAuth(cookie)){
            return redirect.data("data", null);
        }
        
        String userId = catalogController.getUserAuth(cookie);

        if(catalogController.validateUuid(userId)){

            List<Catalog> items = catalogController.parse(elasticService.getCatalogByQuery(
                ElasticQuery.getQueryById(id))
            );

            Catalog item = items.get(0);

            Double amount = 1.00;

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date_now = formatter.format(new Date());

            Cart cartItem = new Cart();
            cartItem.user_id = userId;
            cartItem.date = date_now;
            cartItem.status = "1";
            cartItem.items.add(cartItem.new CartItem(item.id, amount, item.price));

            Gson gson = new Gson();    
            String json = gson.toJson(cartItem);

            //Send to Topic kafka
            cartAddEmitter.send(json);

            return cartAdded.data("item", item);
        }

        return cartAdded.data("item", null);
    }

    @GET
    @Path("/getItem/{id}")
    @Consumes(MediaType.APPLICATION_JSON)                         
    @Produces(MediaType.APPLICATION_JSON)
    public Catalog getCatalogById(@PathParam("id") String id){

        Catalog item = new Catalog();

        if(catalogController.validateUuid(id)){

            List<Catalog> items = catalogController.parse(elasticService.getCatalogByQuery(
                ElasticQuery.getQueryById(id))
            );

            Catalog catalog = items.get(0);
            item = catalog;

            //Gson gson = new Gson();    
            //String json = gson.toJson(cartItem);
        }

        return item;

    }

}