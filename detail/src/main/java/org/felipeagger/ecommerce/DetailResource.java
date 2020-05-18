package org.felipeagger.ecommerce;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.felipeagger.ecommerce.controller.DetailController;
import org.felipeagger.ecommerce.entity.Cart;
import org.felipeagger.ecommerce.entity.Item;
import org.felipeagger.ecommerce.service.ItemService;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/item")                       
@Produces(MediaType.TEXT_HTML)
public class DetailResource {

    @Inject Template detail;
    @Inject Template cartAdded;
    @Inject Template notfound;
    @Inject Template redirect;

    @Inject 
    private DetailController detailController;

    @Inject ItemService itemService;

    @Inject @Channel("cart-topic") Emitter<String> cartAddEmitter;

    @GET
    @Path("/{id}")
    public TemplateInstance getCatalog(@PathParam("id") String id) {

        if(id.isEmpty()){

            return notfound.data("item", "not found");

        } else {
            Item item = itemService.findById(id);

            if(item == null){
                return notfound.data("item", "not found");
            } else {
                return detail.data("detail", item);
            }

        }
        
    }

    @GET
    @Path("/{id}/cart")
    public TemplateInstance setToCart(@HeaderParam("Cookie") String cookie, @PathParam("id") String id) throws ParseException {

        if(!detailController.validateAuth(cookie)){
            return redirect.data("data", null);
        }
        
        String userId = detailController.getUserAuth(cookie);

        if(detailController.validateUuid(userId)){

            Item item = itemService.findById(id);
            Double amount = 1.00;

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date_now = formatter.format(new Date());

            Cart cartItem = new Cart();
            cartItem.user_id = userId;
            cartItem.date = date_now;
            cartItem.status = "1";
            cartItem.items.add(cartItem.new CartItem(item.uuid, amount, item.price));

            Gson gson = new Gson();    
            String json = gson.toJson(cartItem);

            //Send to Topic kafka
            cartAddEmitter.send(json);

            return cartAdded.data("item", item);
        }

        return cartAdded.data("item", null);
    }
}