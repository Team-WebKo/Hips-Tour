package com.project.hiptour.common.reviews.entity;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.global.entity.BaseEntity;
import jakarta.persistence.*;
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
    private Place place;

    @Column(nullable = false)
    private String content;

    private Boolean isLove;

    @ElementCollection
    private List<String> imageUrls;

    private Long userId;
    private String nickname;
}
