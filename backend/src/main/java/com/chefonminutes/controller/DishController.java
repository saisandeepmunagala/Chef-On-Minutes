package com.chefonminutes.controller;

import com.chefonminutes.dto.DishDTO;
import com.chefonminutes.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Read-only browsing of the global dish catalog, e.g. for a chef building their menu. */
@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishRepository dishRepository;

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishes = dishRepository.findByActiveTrue().stream()
                .map(d -> DishDTO.builder().id(d.getId()).name(d.getName()).description(d.getDescription()).build())
                .toList();
        return ResponseEntity.ok(dishes);
    }
}
