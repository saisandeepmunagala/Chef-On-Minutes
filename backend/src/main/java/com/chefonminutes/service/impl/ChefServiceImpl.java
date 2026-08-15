package com.chefonminutes.service.impl;

import com.chefonminutes.dto.ChefDTO;
import com.chefonminutes.dto.ChefDishDTO;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.ChefProfile;
import com.chefonminutes.repository.ChefDishRepository;
import com.chefonminutes.repository.ChefProfileRepository;
import com.chefonminutes.service.ChefService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChefServiceImpl implements ChefService {

    private final ChefProfileRepository chefProfileRepository;
    private final ChefDishRepository chefDishRepository;

    @Override
    public List<ChefDTO> getAllChefs() {
        return chefProfileRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public ChefDTO getChefById(Long chefProfileId) {
        return toDTO(getChefProfileOrThrow(chefProfileId));
    }

    @Override
    public ChefDTO getChefByUserId(Long chefUserId) {
        return toDTO(chefProfileRepository.findByUserId(chefUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef profile not found for user " + chefUserId)));
    }

    @Override
    public ChefDTO updateChefProfile(Long chefUserId, ChefDTO chefDTO) {
        ChefProfile chefProfile = chefProfileRepository.findByUserId(chefUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef profile not found for user " + chefUserId));
        chefProfile.setBio(chefDTO.getBio());
        chefProfile.setSpecialty(chefDTO.getSpecialty());
        if (chefDTO.getAvailable() != null) {
            chefProfile.setAvailable(chefDTO.getAvailable());
        }
        return toDTO(chefProfileRepository.save(chefProfile));
    }

    private ChefProfile getChefProfileOrThrow(Long chefProfileId) {
        return chefProfileRepository.findById(chefProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef not found: " + chefProfileId));
    }

    private ChefDTO toDTO(ChefProfile chefProfile) {
        List<ChefDishDTO> menu = chefDishRepository.findByChefProfileIdAndActiveTrue(chefProfile.getId())
                .stream()
                .map(cd -> ChefDishDTO.builder()
                        .id(cd.getId())
                        .dishId(cd.getDish().getId())
                        .dishName(cd.getDish().getName())
                        .pricePerUnit(cd.getPricePerUnit())
                        .available(cd.getAvailable())
                        .build())
                .toList();
        return ChefDTO.builder()
                .id(chefProfile.getId())
                .userId(chefProfile.getUser().getId())
                .name(chefProfile.getUser().getName())
                .email(chefProfile.getUser().getEmail())
                .phone(chefProfile.getUser().getPhone())
                .bio(chefProfile.getBio())
                .specialty(chefProfile.getSpecialty())
                .available(chefProfile.getAvailable())
                .ratingAvg(chefProfile.getRatingAvg())
                .ratingCount(chefProfile.getRatingCount())
                .menu(menu)
                .build();
    }
}

