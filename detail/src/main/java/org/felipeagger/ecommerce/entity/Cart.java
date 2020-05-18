package org.felipeagger.ecommerce.entity;

import java.util.ArrayList;
import java.util.List;


public class Cart {

    public String user_id; 
    public String date;
    public Double value;
    public String status;
    public List<CartItem> items = new ArrayList<>();

    public class CartItem {
        public String item_uuid;
        public Double amount;
        public Double price;
    
        public CartItem(String itemId, Double amount, Double price){
            this.item_uuid = itemId;
            this.amount = amount;
            this.price = price;
        }
    }

    public List<CartItem> getItems () {
        return this.items;
    }

    public String getStatus(){
        String status_desc = "";
        switch (this.status){
            case "1":
                status_desc = "Open";
                break;
            case "2":
                status_desc = "Closed";
                break;

        }
        return status_desc;
    }

    public void setStatus(String status_desc){
        switch (status_desc){
            case "Open":
                this.status = "1";
                break;
            case "Closed":
                this.status = "2";
                break;

        }
    }

}