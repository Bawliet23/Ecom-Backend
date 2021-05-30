package com.angular.ecommerce.dto;

import com.angular.ecommerce.entities.Product;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;

@Data
public class CartItemDTO {
    private Long id;
    private int quantity;
    private ProductDTO product;
}
