package org.felipeagger.ecommerce;

import com.google.gson.Gson;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.felipeagger.ecommerce.entity.Cart;
import org.felipeagger.ecommerce.service.CartService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CartConsumer {

    @Inject CartService cartService;

    @Incoming("cart-topic")                                                       
    public void process(String cartJson) {

        Gson gson = new Gson();    
        Cart cart = gson.fromJson(cartJson, Cart.class);

        Cart cartDB = cartService.findByUserId(cart.user_id);

        if(cartDB == null){

            cart.value = 0.00;
            cart.items.forEach(item -> {
                cart.value = cart.value + item.amount * item.price;
            });

            cartService.add(cart);

        } else {
            
            cartDB.date = cart.date;

            cart.items.forEach(item -> cartDB.items.add(item)); 
            
            cartDB.value = 0.00;
            cartDB.items.forEach(item -> {
                cartDB.value = cartDB.value + item.amount * item.price;
            });

            cartService.update(cartDB);
        }
        
    }

}