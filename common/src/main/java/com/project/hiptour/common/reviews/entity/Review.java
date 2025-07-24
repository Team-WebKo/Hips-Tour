package com.project.hiptour.common.reviews.entity;

import com.project.hiptour.common.reviews.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Long placeId;

    @Column(nullable = false)
    private String content;

    private Boolean isLove;

    @ElementCollection
    private List<String> imageUrls;

    private Long userId;
    private String nickname;
}
