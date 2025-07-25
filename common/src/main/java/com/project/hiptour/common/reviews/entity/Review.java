package com.project.hiptour.common.reviews.entity;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.global.entity.BaseEntity;
import com.project.hiptour.common.users.entity.User;
import jakarta.persistence.*;

import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @NotNull(message = "여행지를 선택해 주세요.")
    private Place place;

    @Column(nullable = false)
    private String content;

    private Boolean isLove;

    @ElementCollection
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String content, Boolean isLove, List<String> imageUrls) {
        if (content != null) this.content = content;
        if (isLove != null) this.isLove = isLove;
        if (imageUrls != null) this.imageUrls = imageUrls;
    }
}
