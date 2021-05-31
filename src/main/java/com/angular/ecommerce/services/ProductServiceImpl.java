package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.ProductDTO;
import com.angular.ecommerce.entities.Product;
import com.angular.ecommerce.repositories.IProductRepository;
import com.angular.ecommerce.utils.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;



@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Boolean addProduct(ProductDTO productDTO, List<MultipartFile> images) throws IOException {
        if (productDTO.getPrice()>0 && productDTO.getStock() >=0) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImages(FileHandler.uploadFile(images));
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Override
    public void updateProductStock(Long productId, int quantity) {
        if (quantity>=0){
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent())
                 product.get().setStock(quantity);
        }
    }

    @Override
    public int getProductQuantity(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent())
            return product.get().getStock();
        return -1;
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> modelMapper.map(product,ProductDTO.class));
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        ProductDTO productDTO = null;
        if (product.isPresent())
            productDTO = modelMapper.map(product.get(),ProductDTO.class);
        return productDTO;
    }
}
