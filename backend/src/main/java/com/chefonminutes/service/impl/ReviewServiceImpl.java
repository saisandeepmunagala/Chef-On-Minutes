package com.chefonminutes.service.impl;

import com.chefonminutes.dto.ReviewDTO;
import com.chefonminutes.dto.ReviewRequestDTO;
import com.chefonminutes.event.ReviewSubmittedEvent;
import com.chefonminutes.exception.InvalidStateException;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.Booking;
import com.chefonminutes.model.BookingStatus;
import com.chefonminutes.model.Review;
import com.chefonminutes.repository.BookingRepository;
import com.chefonminutes.repository.ReviewRepository;
import com.chefonminutes.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ReviewDTO submitReview(ReviewRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new InvalidStateException("Can only review a COMPLETED booking, current status: " + booking.getStatus());
        }
        if (reviewRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new InvalidStateException("Booking " + booking.getId() + " already has a review");
        }

        Review review = Review.builder()
                .booking(booking)
                .customer(booking.getCustomer())
                .chef(booking.getChef())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        review = reviewRepository.save(review);

        eventPublisher.publishEvent(new ReviewSubmittedEvent(booking.getChef().getId()));

        return toDTO(review);
    }

    @Override
    public List<ReviewDTO> getReviewsForChef(Long chefId) {
        return reviewRepository.findByChefId(chefId).stream().map(this::toDTO).toList();
    }

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .bookingId(review.getBooking().getId())
                .customerId(review.getCustomer().getId())
                .chefId(review.getChef().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
