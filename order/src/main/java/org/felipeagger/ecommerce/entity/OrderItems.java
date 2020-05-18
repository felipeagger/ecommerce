package org.felipeagger.ecommerce.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItems extends PanacheEntityBase {

    @Id private String id;

    private String order_id;
    private String item_uuid;

    @Transient
    private String title;
    @Transient
    private String description;
    @Transient
    private String img_avatar;
    
    private Double amount;
    private Double price;

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return order_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img_avatar;
    }

    public String getItemId() {
        return item_uuid;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPrice() {
        return String.format("%,.2f", this.price);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(String img) {
        this.img_avatar = img;
    }

    public void setOrderId(String user_id) {
        this.order_id = user_id;
    }

    public void setItemId(String item_uuid) {
        this.item_uuid = item_uuid;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
