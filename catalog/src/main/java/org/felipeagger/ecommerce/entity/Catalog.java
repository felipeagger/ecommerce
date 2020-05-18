package org.felipeagger.ecommerce.entity;


public class Catalog {

    public String id; 
    public String title;
    public String description;
    public String img_avatar;
    public String img;
    public Double price;
    public boolean active;

    public String getPrice () {

        return String.format("%,.2f", this.price);

    }

}