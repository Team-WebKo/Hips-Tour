package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "UserPersonalityStat")
public class UserPersonalityStat extends BaseUpdateEntity {
    @Id
    @Column(name = "userId")
    private Long userId;

    @MapsId("userId")
    @OneToOne
    @JoinColumn(name = "userId")
    private UserInfo userInfo;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreO;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreC;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreE;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreA;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreN;

    @Builder
    public UserPersonalityStat(UserInfo userInfo, BigDecimal scoreO, BigDecimal scoreC, BigDecimal scoreE, BigDecimal scoreA, BigDecimal scoreN) {
        this.userId = userInfo.getUserId();
        this.userInfo = userInfo;
        this.scoreO = scoreO;
        this.scoreC = scoreC;
        this.scoreE = scoreE;
        this.scoreA = scoreA;
        this.scoreN = scoreN;
    }
}
