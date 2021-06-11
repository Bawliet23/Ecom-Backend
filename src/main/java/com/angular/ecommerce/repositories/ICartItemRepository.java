package com.angular.ecommerce.repositories;

import com.angular.ecommerce.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Long> {
    Boolean existsCartItemsByProductId(Long productId);
}
