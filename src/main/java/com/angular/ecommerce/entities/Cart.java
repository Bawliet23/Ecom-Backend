package com.angular.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    @Transient
    private double totalPrice;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<CartItem> cartItems = new ArrayList<CartItem>();

    public double getTotalPrice() {
        this.totalPrice=0;
        for (CartItem item : this.getCartItems()){
            totalPrice+=item.getProduct().getPrice()*item.getQuantity();
        }
        return totalPrice;
    }
}
