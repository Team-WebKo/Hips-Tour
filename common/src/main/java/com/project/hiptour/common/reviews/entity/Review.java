package com.project.hiptour.common.reviews.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Long placeId;

    @Column(nullable = false)
    private String content;

    private Boolean isLove;

    @ElementCollection
    private List<String> imageUrls;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long userId;
    private String nickname;
}
