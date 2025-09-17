package com.project.hiptour.common.entity.review;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String headText;
    private String bodyText;

    @ElementCollection
    @CollectionTable(name = "review_image_urls", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "review_hashtags", joinColumns = @JoinColumn(name = "review_id"))
    private List<HashTag> hashTags = new ArrayList<>();

    public void update(String headText, String bodyText, List<String> imageUrls, List<HashTag> hashTags) {
        if (headText != null) this.headText = headText;
        if (bodyText != null) this.bodyText = bodyText;
        if (imageUrls != null) this.imageUrls = imageUrls;
        if (hashTags != null) this.hashTags = hashTags;
    }

//    private Boolean pinned = false;

//    private LocalDateTime pinnedAt;
//    public void pin() {
//        this.pinned = true;
//        this.pinnedAt = LocalDateTime.now(); // 가장 최근에 고정된 리뷰가 위로 오도록

//    }
//    public void unpin() {
//        this.pinned = false;
//        this.pinnedAt = null; // 다시 일반 리뷰처럼 정렬됨

//    }
}
