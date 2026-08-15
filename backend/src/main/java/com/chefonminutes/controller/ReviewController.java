package com.chefonminutes.controller;

import com.chefonminutes.dto.ReviewDTO;
import com.chefonminutes.dto.ReviewRequestDTO;
import com.chefonminutes.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> submitReview(@Valid @RequestBody ReviewRequestDTO request) {
        return ResponseEntity.ok(reviewService.submitReview(request));
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForChef(@PathVariable Long chefId) {
        return ResponseEntity.ok(reviewService.getReviewsForChef(chefId));
    }
}
