package org.felipeagger.ecommerce.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.felipeagger.ecommerce.entity.Item;

import static com.mongodb.client.model.Filters.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ItemService {

    @Inject MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String databaseName;

    public List<Item> list(){
        List<Item> list = new ArrayList<>();
        MongoCursor<Document> cursor = getCollection().find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Item item = new Item();

                item.uuid =document.getString("uuid");
                item.title =document.getString("title");
                item.description =document.getString("description");
                item.price =document.getDouble("price");
                item.active =document.getBoolean("active");
                item.img_avatar =document.getString("img_avatar");
                item.img =document.getString("img");

                list.add(item);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public Item findById(String id){

        MongoCursor<Document> cursor = getCollection().find(eq("uuid", id)).iterator();

        if (cursor.hasNext()){
            Document document = cursor.next();
            Item item = new Item();

            item.uuid = document.getString("uuid");
            item.title = document.getString("title");
            item.description = document.getString("description");
            item.price = document.getDouble("price");
            item.active = document.getBoolean("active");
            item.img_avatar = document.getString("img_avatar");
            item.img = document.getString("img");

            return item;
        } else {
            return null;
        }
        
    }


    public void add(Item item){
        Document document = new Document()
                .append("uuid", item.uuid)
                .append("title", item.title)
                .append("description", item.description)
                .append("price", item.price)
                .append("active", item.active)
                .append("img_avatar", item.img_avatar)
                .append("img", item.img);
        getCollection().insertOne(document);
    }

    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase(databaseName).getCollection("Items");
    }
}