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
    public Heart(boolean isActive, UserInfo userId, Place feedId) {
        this.isActive = isActive;
        this.userId = userId;
        this.feedId = feedId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userId;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Place feedId;




}
