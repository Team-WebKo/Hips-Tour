package com.project.hiptour.common.entity.perference;

import com.project.hiptour.common.entity.review.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ReviewSelections")
public class ReviewSelections {
    @EmbeddedId
    private ReviewSelectionsId id;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordId")
    private Review review;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordId")
    private OceanMaster oceanMaster;

    @Builder
    public ReviewSelections(Review review, OceanMaster oceanMaster) {
        this.review = review;
        this.oceanMaster = oceanMaster;
        this.id = new ReviewSelectionsId(review.getReviewId(), oceanMaster.getKeywordId());
    }
}
