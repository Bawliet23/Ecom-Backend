package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.CartDTO;
import com.angular.ecommerce.dto.CartItemDTO;
import com.angular.ecommerce.dto.RegisterDTO;
import com.angular.ecommerce.entities.User;

public interface IUserService {
    User addUser(RegisterDTO user);
    Boolean addToCart(Long cartId ,CartItemDTO cartItemDTO);
    Boolean emptyCart(Long cartId);
    Boolean deleteCartItem(Long cartItemId);
    Boolean updateCartItemQuantity(Long cartItemId,int quantity);
    CartDTO getCart(Long Id);
}
