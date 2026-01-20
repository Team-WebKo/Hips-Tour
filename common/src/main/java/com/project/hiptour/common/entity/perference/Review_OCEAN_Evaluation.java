package com.project.hiptour.common.entity.perference;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.review.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Review_OCEAN_Evaluation")
@Builder
@AllArgsConstructor
public class Review_OCEAN_Evaluation extends BaseUpdateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId")
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(name = "dimension", nullable = false)
    private DimensionType dimension;

    @Column(name = "preScore", precision = 5, scale = 3)
    private BigDecimal preScore;

    @Column(name = "deltaScore", precision = 5, scale = 3)
    private BigDecimal deltaScore;

    @Column(name = "appliedL", precision = 3, scale = 2)
    private BigDecimal appliedL;
}
