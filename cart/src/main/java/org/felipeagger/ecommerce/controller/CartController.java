package org.felipeagger.ecommerce.controller;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.felipeagger.ecommerce.entity.Auth;
import org.felipeagger.ecommerce.service.AuthService;

@ApplicationScoped
public class CartController {

    @Inject @RestClient AuthService authService;

    public Boolean validateUuid(String uuid) {

        try{
            UUID.fromString(uuid);            
            return true;
        } catch (IllegalArgumentException exception){
            return false;
        }

    }

    public Boolean validateAuth(String cookie) {
        
        if(cookie != null){
            String token = cookie.substring(cookie.indexOf("token=")+6, cookie.length()); 

            if(!token.isEmpty()){

                String json = String.format("{\"token\":\"%s\"}", token);

                try {
                    Auth auth = authService.validateToken(json);

                    if(auth.status.equals("Authorized")){
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return false;
    }

    public String getUserAuth(String cookie) {
        
        return cookie.substring(cookie.indexOf("userId=")+7, 43);

    }

}