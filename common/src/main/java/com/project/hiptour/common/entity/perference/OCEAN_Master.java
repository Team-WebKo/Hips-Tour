package com.project.hiptour.common.entity.perference;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "OCEAN_Master")
public class OCEAN_Master extends BaseUpdateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keywordId")
    private Integer keywordId;

    @Column(name = "label", nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetDimension", nullable = false)
    private DimensionType targetDimension;

    @Builder
    public OCEAN_Master(Integer keywordId, String label, DimensionType targetDimension) {
        this.keywordId = keywordId;
        this.label = label;
        this.targetDimension = targetDimension;
    }
}
