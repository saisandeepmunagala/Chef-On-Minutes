package com.chefonminutes.service;

import com.chefonminutes.dto.ReviewDTO;
import com.chefonminutes.dto.ReviewRequestDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO submitReview(ReviewRequestDTO request);
    List<ReviewDTO> getReviewsForChef(Long chefId);
}
