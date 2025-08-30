package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class TokenInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seq;
    private long userId;
    private String refreshToken;
    private boolean isActive;
}
