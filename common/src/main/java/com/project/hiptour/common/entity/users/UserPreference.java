package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
@Table(name = "user_preference")
public class UserPreference extends BaseUpdateEntity {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @Column(precision = 4, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal openness = new BigDecimal("5.00");

    @Column(precision = 4, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal conscientiousness = new BigDecimal("5.00");

    @Column(precision = 4, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal extraversion = new BigDecimal("5.00");

    @Column(precision = 4, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal agreeableness = new BigDecimal("5.00");

    @Column(precision = 4, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal neuroticism = new BigDecimal("5.00");

    @Version
    @Column(nullable = false)
    private Integer version;
}
