package com.chefonminutes.repository;

import com.chefonminutes.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByActiveTrue();
}
