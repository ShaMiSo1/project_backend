package com.example.bangyo.repository;

import com.example.bangyo.entity.User;
import com.example.bangyo.entity.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {
    // 특정 유저의 냉장고 재료 조회
    List<UserIngredient> findByUser(User user);
}
