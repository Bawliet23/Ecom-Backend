package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.ProductDTO;
import com.angular.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    ProductDTO addProduct(Product product);
    void addStockQuantity(Long productId, int quantity);
    int getProductQuantity(Long productId);
    Page<ProductDTO> getAllProducts(Pageable pageable);
    ProductDTO getProductById(Long productId);


}
