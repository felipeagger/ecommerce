package org.felipeagger.ecommerce;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.felipeagger.ecommerce.controller.CartController;
import org.felipeagger.ecommerce.entity.Cart;
import org.felipeagger.ecommerce.service.CartService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.google.gson.Gson;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/cart")                       
@Produces(MediaType.TEXT_HTML)
public class CartResource {

    @Inject Template cart;
    @Inject Template order;
    @Inject Template redirect;

    @Inject CartService cartService;

    @Inject 
    private CartController cartController;

    @Inject @Channel("order-topic") Emitter<String> orderAddEmitter;

    @GET
    @Path("/")
    public TemplateInstance getCart(@HeaderParam("Cookie") String cookie) {

        if(!cartController.validateAuth(cookie)){
            return redirect.data("data", null);
        }

        String userId = cartController.getUserAuth(cookie);

        if(cartController.validateUuid(userId)){
            Cart cartData = cartService.findCompletedByUserId(userId);

            return cart.data("cartData", cartData);
        }

        return cart.data("cartData", null);
    }

    @POST
    @Path("/{itemid}/delete")
    public TemplateInstance deleteCartItem(@HeaderParam("Cookie") String cookie, @PathParam("itemid") String itemId) {

        if(!cartController.validateAuth(cookie)){
            return redirect.data("data", null);
        }
        
        String userId = cartController.getUserAuth(cookie);

        if(cartController.validateUuid(userId) && cartController.validateUuid(itemId)){

            cartService.deleteItemCart(userId, itemId);    
            
            Cart cartData = cartService.findCompletedByUserId(userId);

            return cart.data("cartData", cartData);
        }

        return cart.data("cartData", null);
    }

    @POST
    @Path("/buy")
    public TemplateInstance buyCart(@HeaderParam("Cookie") String cookie) {

        if(!cartController.validateAuth(cookie)){
            return redirect.data("data", null);
        }
        
        String userId = cartController.getUserAuth(cookie);

        if(cartController.validateUuid(userId)){

            Cart cartData = cartService.findByUserId(userId);

            if(cartData == null || cartData.items == null){
                return order.data("items", false);
            }

            Gson gson = new Gson();    
            String json = gson.toJson(cartData);

            //Send to Topic kafka
            orderAddEmitter.send(json);

            cartService.deleteCart(userId);

            return order.data("data", true);
        }

        return order.data("data", false);
    }

}