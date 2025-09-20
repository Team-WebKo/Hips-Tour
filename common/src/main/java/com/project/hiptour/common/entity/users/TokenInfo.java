package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
@NoArgsConstructor
@Getter
public class TokenInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seq;
    private long userId;
    private String refreshToken;
    private boolean isActive;

    @Builder
    public TokenInfo(long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
    public boolean isStillAvailable(LocalDateTime curTime){
        return !this.isActive || this.getCreatedAt().isBefore(curTime);
    }
}
