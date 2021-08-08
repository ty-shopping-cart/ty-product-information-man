package com.trendyol.typroductinformationman.repository;

import com.trendyol.typroductinformationman.model.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock,Long> {
    ProductStock findByProduct_Id(Long productId);
}
