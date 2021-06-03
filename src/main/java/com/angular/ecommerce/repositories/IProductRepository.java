package com.angular.ecommerce.repositories;
import com.angular.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findProductsByNameContainingOrDesignationContaining(Pageable pageable,String name,String designation);
}
