package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.ProductDTO;
import com.angular.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Boolean addProduct(ProductDTO productDTO, List<MultipartFile> images) throws IOException;
    void updateProductStock(Long productId, int quantity);
    int getProductQuantity(Long productId);
    Page<ProductDTO> getAllProducts(Pageable pageable);
    ProductDTO getProductById(Long productId);
    Page<ProductDTO> searchProduct(Pageable pageable, String serchKey);
    ProductDTO updateProduct(ProductDTO productDTO);
    Boolean deleteProduct(Long productId);


}
