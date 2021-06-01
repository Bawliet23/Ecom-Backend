package com.angular.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @Transient
    private double totalPrice;
    private String shippingAddress;
    @ManyToOne(fetch = FetchType.LAZY,cascade ={
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JsonIgnore
    private User user;
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public double getTotalPrice() {
        this.totalPrice=0;
        for (OrderItem item : this.getOrderItems()){
            totalPrice+=item.getProduct().getPrice()*item.getQuantity();
        }
        return totalPrice;
    }
}
