package org.felipeagger.ecommerce.entity;

public class ItemCart {

    public Item item = new Item(); 
    public Double amount = 0.00;

    public Double getTotal() {
        return this.item.price * this.amount;
    }

    public String getTotalStr() {
        return String.format("%,.2f", this.getTotal());
    }
    
}