package com.project.hiptour.common.entity.perference;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.Place;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PlacePersonalityStat")
public class PlacePersonalityStat extends BaseUpdateEntity {
    @Id
    @Column(name = "placeId")
    private Integer placeId;

    @MapsId("placeId")
    @OneToOne
    @JoinColumn(name = "placeId")
    private Place place;

    // Openess
    @Column(name = "positiveCountO", columnDefinition = "INT DEFAULT 0")
    private Integer positiveCountO;

    @Column(name = "totalCountO", columnDefinition = "INT DEFAULT 0")
    private Integer totalCountO;

    @Column(name = "scoreO", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreO;

    // Conscientiousness
    @Column(name = "positiveCountC", columnDefinition = "INT DEFAULT 0")
    private Integer positiveCountC;

    @Column(name = "totalCountC", columnDefinition = "INT DEFAULT 0")
    private Integer totalCountC;

    @Column(name = "scoreC", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreC;

    // Extraversion
    @Column(name = "positiveCountE", columnDefinition = "INT DEFAULT 0")
    private Integer positiveCountE;

    @Column(name = "totalCountE", columnDefinition = "INT DEFAULT 0")
    private Integer totalCountE;

    @Column(name = "scoreE", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreE;

    // Agreeableness
    @Column(name = "positiveCountA", columnDefinition = "INT DEFAULT 0")
    private Integer positiveCountA;

    @Column(name = "totalCountA", columnDefinition = "INT DEFAULT 0")
    private Integer totalCountA;

    @Column(name = "scoreA", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreA;

    // Neuroticism
    @Column(name = "positiveCountN", columnDefinition = "INT DEFAULT 0")
    private Integer positiveCountN;

    @Column(name = "totalCountN", columnDefinition = "INT DEFAULT 0")
    private Integer totalCountN;

    @Column(name = "scoreN", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal scoreN;

}
