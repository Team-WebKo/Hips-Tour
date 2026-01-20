package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.perference.DimensionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "UserPersonalityStat")
public class UserPersonalityStat extends BaseUpdateEntity {
    @EmbeddedId
    private UserPersonalityStatId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserInfo userInfo;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal score;

    @Version
    @Column(name = "version")
    private Integer version;    //낙관적 락

    @Builder
    public UserPersonalityStat(UserInfo userInfo, DimensionType dimension, BigDecimal score, Integer version) {
        this.userInfo = userInfo;
        this.score = score;
        this.version = version;
        this.id = new UserPersonalityStatId(userInfo.getUserId(), dimension);
    }
}
