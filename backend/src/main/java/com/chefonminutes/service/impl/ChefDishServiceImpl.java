package com.chefonminutes.service.impl;

import com.chefonminutes.dto.ChefDishDTO;
import com.chefonminutes.dto.ChefDishRequestDTO;
import com.chefonminutes.exception.AccessDeniedException;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.ChefDish;
import com.chefonminutes.model.ChefProfile;
import com.chefonminutes.model.Dish;
import com.chefonminutes.repository.ChefDishRepository;
import com.chefonminutes.repository.ChefProfileRepository;
import com.chefonminutes.repository.DishRepository;
import com.chefonminutes.service.ChefDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChefDishServiceImpl implements ChefDishService {

    private final ChefDishRepository chefDishRepository;
    private final ChefProfileRepository chefProfileRepository;
    private final DishRepository dishRepository;

    @Override
    public List<ChefDishDTO> getMenu(Long chefProfileId) {
        return chefDishRepository.findByChefProfileIdAndActiveTrue(chefProfileId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public ChefDishDTO addToMenu(Long chefUserId, ChefDishRequestDTO request) {
        ChefProfile chefProfile = chefProfileRepository.findByUserId(chefUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef profile not found for user " + chefUserId));
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + request.getDishId()));

        ChefDish chefDish = ChefDish.builder()
                .chefProfile(chefProfile)
                .dish(dish)
                .pricePerUnit(request.getPricePerUnit())
                .available(request.getAvailable() == null || request.getAvailable())
                .build();
        return toDTO(chefDishRepository.save(chefDish));
    }

    @Override
    public ChefDishDTO updateMenuItem(Long chefUserId, Long chefDishId, ChefDishRequestDTO request) {
        ChefDish chefDish = getOwnedChefDish(chefUserId, chefDishId);
        chefDish.setPricePerUnit(request.getPricePerUnit());
        if (request.getAvailable() != null) {
            chefDish.setAvailable(request.getAvailable());
        }
        return toDTO(chefDishRepository.save(chefDish));
    }

    @Override
    public void removeFromMenu(Long chefUserId, Long chefDishId) {
        ChefDish chefDish = getOwnedChefDish(chefUserId, chefDishId);
        chefDish.setActive(false);
        chefDishRepository.save(chefDish);
    }

    private ChefDish getOwnedChefDish(Long chefUserId, Long chefDishId) {
        ChefDish chefDish = chefDishRepository.findById(chefDishId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef dish not found: " + chefDishId));
        if (!chefDish.getChefProfile().getUser().getId().equals(chefUserId)) {
            throw new AccessDeniedException("You do not own this menu item");
        }
        return chefDish;
    }

    private ChefDishDTO toDTO(ChefDish chefDish) {
        return ChefDishDTO.builder()
                .id(chefDish.getId())
                .dishId(chefDish.getDish().getId())
                .dishName(chefDish.getDish().getName())
                .pricePerUnit(chefDish.getPricePerUnit())
                .available(chefDish.getAvailable())
                .build();
    }
}
