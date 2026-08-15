package com.chefonminutes.controller;

import com.chefonminutes.dto.ChefDTO;
import com.chefonminutes.service.ChefService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints for browsing chefs and managing a chef's own profile.
 * Account creation happens via /api/auth/register (role=CHEF).
 */
@RestController
@RequestMapping("/api/chefs")
@RequiredArgsConstructor
public class ChefController {

    private final ChefService chefService;

    @GetMapping
    public ResponseEntity<List<ChefDTO>> getAllChefs() {
        return ResponseEntity.ok(chefService.getAllChefs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChefDTO> getChefById(@PathVariable Long id) {
        return ResponseEntity.ok(chefService.getChefById(id));
    }

    // TODO: chefUserId should come from the authenticated principal once JWT/session auth is implemented
    @GetMapping("/me")
    public ResponseEntity<ChefDTO> getOwnProfile(@RequestParam Long chefUserId) {
        return ResponseEntity.ok(chefService.getChefByUserId(chefUserId));
    }

    // TODO: chefUserId should come from the authenticated principal once JWT/session auth is implemented
    @PutMapping("/me")
    public ResponseEntity<ChefDTO> updateOwnProfile(@RequestParam Long chefUserId, @Valid @RequestBody ChefDTO chefDTO) {
        return ResponseEntity.ok(chefService.updateChefProfile(chefUserId, chefDTO));
    }
}

