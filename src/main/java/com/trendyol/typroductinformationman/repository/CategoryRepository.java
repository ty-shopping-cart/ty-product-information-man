package com.trendyol.typroductinformationman.repository;

import com.trendyol.typroductinformationman.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
