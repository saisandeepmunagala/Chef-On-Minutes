package com.chefonminutes.repository;

import com.chefonminutes.model.ChefDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChefDishRepository extends JpaRepository<ChefDish, Long> {
    List<ChefDish> findByChefProfileIdAndActiveTrue(Long chefProfileId);
}
