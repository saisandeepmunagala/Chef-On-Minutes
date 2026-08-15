package com.chefonminutes.repository;

import com.chefonminutes.model.ChefProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChefProfileRepository extends JpaRepository<ChefProfile, Long> {
    Optional<ChefProfile> findByUserId(Long userId);
}
