package org.felipeagger.ecommerce.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="orders")
public class Order extends PanacheEntityBase {

    @Id private String id;

    private String user_id;

    private Integer order;

    private Date date;

    private Double value;

    private Integer status;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;


    public String getId() {
        return id;
    }

    public String getUserId() {
        return user_id;
    }

    public Integer getOrder() {
        return order;
    }

    public String getDate() {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.date);
    }

    public String getValue() {
        return String.format("%,.2f", this.value);
    }

    public String getStatus(){
        String status_desc = "";
        switch (this.status){
            case 1:
                status_desc = "Processing";
                break;
            case 2:
                status_desc = "Sent";
                break;
            case 3:
                status_desc = "Finished";
                break;

        }
        return status_desc;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
