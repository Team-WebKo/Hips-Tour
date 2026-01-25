package com.project.hiptour.common.entity.perference;

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
@Table(name = "OceanMaster")
public class OceanMaster extends BaseUpdateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keywordId")
    private Integer keywordId;

    @Column(name = "label", nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetDimension", nullable = false)
    private DimensionType targetDimension;

    @Column(name = "polarityWeight", precision = 3, scale = 2)
    private BigDecimal polarityWeight;

}
