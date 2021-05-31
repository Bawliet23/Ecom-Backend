package com.angular.ecommerce.entities;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String name;
    private String designation;
    private String description;
    @Positive
    private double price;
    @Positive
    private int stock;
    @ElementCollection
    List<String> images = new ArrayList<String>();
}
