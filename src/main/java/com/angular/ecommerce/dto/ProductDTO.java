package com.angular.ecommerce.dto;

import com.angular.ecommerce.utils.Env;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String designation;
    private String description;
    private double price;
    private int stock;
    List<String> images = new ArrayList<String>();

    public void setImages(List<String> images) {
        this.images = images.stream().map(image -> Env.getUrlImages()+image).collect(Collectors.toList());
    }
}
