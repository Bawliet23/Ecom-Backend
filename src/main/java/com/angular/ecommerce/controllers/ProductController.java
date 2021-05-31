package com.angular.ecommerce.controllers;

import com.angular.ecommerce.dto.ProductDTO;
import com.angular.ecommerce.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Page<ProductDTO>> getProducts(@PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.ok()
                .body(productService.getAllProducts(page));

    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id){
        return ResponseEntity.ok()
                .body(productService.getProductById(id));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestParam("images") List<MultipartFile> images,@RequestParam("product") String  productDTO) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO1 = objectMapper.readValue(productDTO,ProductDTO.class);
        Boolean added = productService.addProduct(productDTO1,images);
        if (added)
        return ResponseEntity.ok().body("product added");
        return ResponseEntity.badRequest().body("product Not added");
    }
}
