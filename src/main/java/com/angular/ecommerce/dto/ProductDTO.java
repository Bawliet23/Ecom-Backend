package com.angular.ecommerce.dto;

import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String designation;
    private String description;
    private double price;
    private int stock;
    List<String> images = new ArrayList<String>();
}
