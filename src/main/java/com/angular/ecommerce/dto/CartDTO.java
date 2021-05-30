package com.angular.ecommerce.dto;

import com.angular.ecommerce.entities.CartItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private double totalPrice;
    private List<CartItemDTO> cartItems = new ArrayList<CartItemDTO>();
}
