package com.trendyol.typroductinformationman.repository;

import com.trendyol.typroductinformationman.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
