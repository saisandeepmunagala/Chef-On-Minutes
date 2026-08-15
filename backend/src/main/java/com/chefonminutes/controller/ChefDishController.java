package com.chefonminutes.controller;

import com.chefonminutes.dto.ChefDishDTO;
import com.chefonminutes.dto.ChefDishRequestDTO;
import com.chefonminutes.service.ChefDishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Chef self-service menu management. chefUserId is a request param placeholder for the
 * authenticated principal until JWT/session auth is implemented (see plan scope boundaries).
 */
@RestController
@RequiredArgsConstructor
public class ChefDishController {

    private final ChefDishService chefDishService;

    @GetMapping("/api/chefs/{chefProfileId}/dishes")
    public ResponseEntity<List<ChefDishDTO>> getMenu(@PathVariable Long chefProfileId) {
        return ResponseEntity.ok(chefDishService.getMenu(chefProfileId));
    }

    @PostMapping("/api/chefs/me/dishes")
    public ResponseEntity<ChefDishDTO> addToMenu(@RequestParam Long chefUserId,
                                                  @Valid @RequestBody ChefDishRequestDTO request) {
        return ResponseEntity.ok(chefDishService.addToMenu(chefUserId, request));
    }

    @PutMapping("/api/chefs/me/dishes/{chefDishId}")
    public ResponseEntity<ChefDishDTO> updateMenuItem(@RequestParam Long chefUserId,
                                                       @PathVariable Long chefDishId,
                                                       @Valid @RequestBody ChefDishRequestDTO request) {
        return ResponseEntity.ok(chefDishService.updateMenuItem(chefUserId, chefDishId, request));
    }

    @DeleteMapping("/api/chefs/me/dishes/{chefDishId}")
    public ResponseEntity<Void> removeFromMenu(@RequestParam Long chefUserId, @PathVariable Long chefDishId) {
        chefDishService.removeFromMenu(chefUserId, chefDishId);
        return ResponseEntity.noContent().build();
    }
}
