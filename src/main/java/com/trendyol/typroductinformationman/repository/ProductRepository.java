package com.trendyol.typroductinformationman.repository;

import com.trendyol.typroductinformationman.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
