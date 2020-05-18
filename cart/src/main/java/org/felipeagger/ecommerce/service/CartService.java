package org.felipeagger.ecommerce.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.felipeagger.ecommerce.entity.Cart;
import org.felipeagger.ecommerce.entity.ItemCart;

import static com.mongodb.client.model.Filters.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CartService {

    @Inject MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String databaseName;

    public Cart findByUserId(String userId){

        MongoCursor<Document> cursor = getCollection("Cart").find(eq("user_id", userId)).iterator();

        if (cursor.hasNext()){
            Document document = cursor.next();
            Cart cart = new Cart();

            cart.user_id = document.getString("user_id");
            cart.date = document.getString("date");
            cart.value = document.getDouble("value");
            cart.status = document.getString("status");

            List<Document> listDocs = (List<Document>)document.get("items");

            listDocs.forEach(item -> {
                Cart.CartItem newItem = cart.new CartItem();

                newItem.item_uuid = item.getString("item_uuid");
                newItem.amount = item.getDouble("amount");
                newItem.price = item.getDouble("price");

                cart.items.add(newItem);
            });

            return cart;
        } else {
            return null;
        }
        
    }


    public Cart findCompletedByUserId(String userId){

        MongoCursor<Document> cursor = getCollection("Cart").find(eq("user_id", userId)).iterator();

        if (cursor.hasNext()){
            Document document = cursor.next();
            Cart cart = new Cart();

            cart.user_id = document.getString("user_id");
            cart.date = document.getString("date");
            cart.value = document.getDouble("value");
            cart.status = document.getString("status");

            List<Document> listDocs = (List<Document>)document.get("items");

            listDocs.forEach(item -> {

                ItemCart itemCart = this.findItemById(item.getString("item_uuid"));

                itemCart.amount = item.getDouble("amount");
                itemCart.item.price = item.getDouble("price");

                cart.itemsCart.add(itemCart);
            });

            return cart;
        } else {
            return null;
        }
        
    }


    public void add(Cart cart){
        Document document = new Document()
                .append("user_id", cart.user_id)
                .append("date", cart.date)
                .append("value", cart.value)
                .append("status", cart.status);

        List<Document> itemsDoc = new ArrayList<>();

        cart.items.forEach(item -> {

            Document docItem = new Document()
                .append("item_uuid", item.item_uuid)
                .append("amount", item.amount)
                .append("price", item.price);

            itemsDoc.add(docItem);
        });

        document.append("items", itemsDoc);

        getCollection("Cart").insertOne(document);
    }


    public void update(Cart cart){
        Document document = new Document()
                .append("user_id", cart.user_id)
                .append("date", cart.date)
                .append("value", cart.value)
                .append("status", cart.status);

        List<Document> itemsDoc = new ArrayList<>();

        cart.items.forEach(item -> {

            Document docItem = new Document()
                .append("item_uuid", item.item_uuid)
                .append("amount", item.amount)
                .append("price", item.price);

            itemsDoc.add(docItem);
        });

        document.append("items", itemsDoc);

        getCollection("Cart").updateOne(eq("user_id", cart.user_id), new Document("$set",  document));
    }

    public ItemCart findItemById(String id){

        MongoCursor<Document> cursor = getCollection("Items").find(eq("uuid", id)).iterator();

        if (cursor.hasNext()){
            Document document = cursor.next();
            ItemCart itemCart = new ItemCart();

            itemCart.item.uuid = document.getString("uuid");
            itemCart.item.title = document.getString("title");
            itemCart.item.description = document.getString("description");
            itemCart.item.price = document.getDouble("price");
            itemCart.item.active = document.getBoolean("active");
            itemCart.item.img_avatar = document.getString("img_avatar");
            itemCart.item.img = document.getString("img");

            return itemCart;
        } else {
            return null;
        }
        
    }

    public Cart deleteItemCart(String userId, String itemId){

        Cart cart = this.findByUserId(userId);

        cart.items.removeIf(item -> item.item_uuid.equals(itemId));

        cart.value = 0.00;
        cart.items.forEach(item -> {
            cart.value = cart.value + item.amount *item.price;
        });

        this.update(cart);

        return cart;
    }

    public Boolean deleteCart(String userId){

        getCollection("Cart").deleteOne(eq("user_id", userId));

        return true;
    }

    private MongoCollection<Document> getCollection(String name){
        return mongoClient.getDatabase(databaseName).getCollection(name);
    }
}