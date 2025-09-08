package com.project.hiptour.common.entity.heart;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Heart extends BaseTimeEntity {

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
