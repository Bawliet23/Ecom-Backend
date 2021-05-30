package com.angular.ecommerce.dto;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String designation;
    private double price;
    private int stock;
    List<String> images = new ArrayList<String>();
}
