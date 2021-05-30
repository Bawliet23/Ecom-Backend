package com.angular.ecommerce.controllers;

import com.angular.ecommerce.dto.ProductDTO;
import com.angular.ecommerce.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Page<ProductDTO>> getBooks(@PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.ok()
                .body(productService.getAllProducts(page));

    }
}
