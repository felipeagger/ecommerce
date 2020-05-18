package org.felipeagger.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.felipeagger.ecommerce.entity.Auth;
import org.felipeagger.ecommerce.entity.Catalog;
import org.felipeagger.ecommerce.service.AuthService;

@ApplicationScoped
public class CatalogController {

    @Inject @RestClient AuthService authService;

    public List<Catalog> parse(Object objeto) {
        
        JsonElement root = new JsonParser().parse(new Gson().toJson(objeto, Object.class));

        JsonArray arrayjson = root.getAsJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();

        List<Catalog> catalogItems = new ArrayList<>();

        arrayjson.forEach(item -> {
            catalogItems.add(new Gson().fromJson(item.getAsJsonObject().get("_source").getAsJsonObject(), Catalog.class));
        });

        return catalogItems;
    }

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