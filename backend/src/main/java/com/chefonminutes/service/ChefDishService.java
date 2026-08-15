package com.chefonminutes.service;

import com.chefonminutes.dto.ChefDishDTO;
import com.chefonminutes.dto.ChefDishRequestDTO;

import java.util.List;

/** Chef self-service menu management: a chef owns and curates their own ChefDish entries. */
public interface ChefDishService {

    List<ChefDishDTO> getMenu(Long chefProfileId);

    ChefDishDTO addToMenu(Long chefUserId, ChefDishRequestDTO request);

    ChefDishDTO updateMenuItem(Long chefUserId, Long chefDishId, ChefDishRequestDTO request);

    void removeFromMenu(Long chefUserId, Long chefDishId);
}
