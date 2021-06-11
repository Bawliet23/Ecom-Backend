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
import java.util.Optional;

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
    @GetMapping("/search/{searchKey}")
    public ResponseEntity<?> searchByDescription(Pageable pageable, @PathVariable(name="searchKey") String searchKey){

        System.out.println(searchKey);
        if (searchKey.isEmpty()) {
            ResponseEntity.ok().body(productService.getAllProducts(pageable));
        }
        return ResponseEntity.ok().body(productService.searchProduct(pageable, searchKey));
    }
    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO){
        ProductDTO productDTO1 = productService.updateProduct(productDTO);
        if (productDTO1==null)
            return ResponseEntity.badRequest().body("Update Failed");
        return  ResponseEntity.ok().body(productDTO1);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId){
        Boolean deleted = productService.deleteProduct(productId);
        if(!deleted)
            return ResponseEntity.badRequest().body("Item Not Deleted");
        return ResponseEntity.ok("Item Deleted");
    }
}
