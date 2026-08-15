package com.chefonminutes.event;

/** Published when a Review is submitted; listener recomputes the chef's ratingAvg/ratingCount. */
public class ReviewSubmittedEvent {
    private final Long chefUserId;

    public ReviewSubmittedEvent(Long chefUserId) {
        this.chefUserId = chefUserId;
    }

    public Long getChefUserId() {
        return chefUserId;
    }
}
