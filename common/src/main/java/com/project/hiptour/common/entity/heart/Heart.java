package com.project.hiptour.common.entity.heart;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Heart extends BaseTimeEntity {

    @Builder
    public Heart(boolean isActive, UserInfo user, Place feed) {
        this.isActive = isActive;
        this.user = user;
        this.feed = feed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Place feed;




}
