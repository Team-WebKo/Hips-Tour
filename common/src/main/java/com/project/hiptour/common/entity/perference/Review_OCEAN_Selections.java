package com.project.hiptour.common.entity.perference;

import com.project.hiptour.common.entity.review.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Review_OCEAN_Selections")
public class Review_OCEAN_Selections {
    @EmbeddedId
    private Review_OCEAN_SelectionsId id;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordId")
    private Review review;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordId")
    private OCEAN_Master oceanMaster;

    @Builder
    public Review_OCEAN_Selections(Review review, OCEAN_Master oceanMaster) {
        this.review = review;
        this.oceanMaster = oceanMaster;
        this.id = new Review_OCEAN_SelectionsId(review.getReviewId(), oceanMaster.getKeywordId());
    }
}
