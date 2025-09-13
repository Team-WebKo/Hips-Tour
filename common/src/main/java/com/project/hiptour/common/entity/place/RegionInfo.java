package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class RegionInfo extends BaseUpdateEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer regionId;
    private String regionName;

    @Column(unique = true)
    private String areaCode;

}
