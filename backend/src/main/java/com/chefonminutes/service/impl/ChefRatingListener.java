package com.chefonminutes.service.impl;

import com.chefonminutes.event.ReviewSubmittedEvent;
import com.chefonminutes.repository.ChefProfileRepository;
import com.chefonminutes.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/** Sole owner of ChefProfile.ratingAvg/ratingCount - recomputes them whenever a review is submitted. */
@Component
@RequiredArgsConstructor
public class ChefRatingListener {

    private final ReviewRepository reviewRepository;
    private final ChefProfileRepository chefProfileRepository;

    @EventListener
    public void onReviewSubmitted(ReviewSubmittedEvent event) {
        List<com.chefonminutes.model.Review> reviews = reviewRepository.findByChefId(event.getChefUserId());
        if (reviews.isEmpty()) {
            return;
        }
        double avg = reviews.stream().mapToInt(com.chefonminutes.model.Review::getRating).average().orElse(0.0);
        chefProfileRepository.findByUserId(event.getChefUserId()).ifPresent(chefProfile -> {
            chefProfile.setRatingAvg(avg);
            chefProfile.setRatingCount(reviews.size());
            chefProfileRepository.save(chefProfile);
        });
    }
}
