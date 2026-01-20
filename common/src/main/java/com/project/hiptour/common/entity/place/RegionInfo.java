package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "RegionInfo")
public class RegionInfo extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Integer regionId;

    @Column(nullable = false)
    private String regionName;

    @Column(unique = true, nullable = false)
    private String areaCode;

    @Builder
    public RegionInfo(Integer regionId, String regionName, String areaCode) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.areaCode = areaCode;
    }
}
