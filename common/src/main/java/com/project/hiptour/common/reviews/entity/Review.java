package com.project.hiptour.common.reviews.entity;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.review.HashTag;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.global.entity.BaseEntity;
import com.project.hiptour.common.users.entity.User;
import jakarta.persistence.*;

import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String headText;
    private String bodyText;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "review_hashtags", joinColumns = @JoinColumn(name = "review_id"))
    private List<HashTag> hashTags = new ArrayList<>();

    public void update(String headText, String bodyText, List<HashTag> hashTags) {
        if (headText != null) {
            this.headText = headText;
        }

        if (bodyText != null) {
            this.bodyText = bodyText;
        }

        if (hashTags != null) {
            this.hashTags = hashTags;
        }
    }
}
