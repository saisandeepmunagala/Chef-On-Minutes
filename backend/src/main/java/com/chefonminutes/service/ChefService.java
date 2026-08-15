package com.chefonminutes.service;

import com.chefonminutes.dto.ChefDTO;

import java.util.List;

/**
 * Business logic contract for chef browsing/profile management.
 * Chef account creation happens via AuthService.register(role=CHEF); this service
 * only reads/updates the resulting ChefProfile.
 */
public interface ChefService {

    List<ChefDTO> getAllChefs();

    ChefDTO getChefById(Long chefProfileId);

    ChefDTO getChefByUserId(Long chefUserId);

    ChefDTO updateChefProfile(Long chefUserId, ChefDTO chefDTO);
}

