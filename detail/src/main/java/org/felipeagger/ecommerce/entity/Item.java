package org.felipeagger.ecommerce.entity;


public class Item {

    public String uuid; 
    public String title;
    public String description;
    public String img_avatar;
    public String img;
    public Double price;
    public boolean active;

    public Item() {

    }

    public String getPrice () {
        return String.format("%,.2f", this.price);
    }

}