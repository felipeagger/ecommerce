package org.felipeagger.ecommerce.entity;

import java.util.ArrayList;
import java.util.List;


public class Cart {

    public String user_id = ""; 
    public String date;
    public Double value = 0.00;
    public String status = "1";
    public List<CartItem> items = new ArrayList<>();

    //Virtual Field
    public List<ItemCart> itemsCart = new ArrayList<>();

    public Cart(){}

    public class CartItem {

        public String item_uuid = "";
        public Double amount = 0.00;
        public Double price = 0.00;

        public CartItem(){}
    }

    public List<CartItem> getItems () {
        return this.items;
    }

    public String getTotal () {
        return String.format("%,.2f", this.value);
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