package com.project.hiptour.common.entity.place.repos;

import com.project.hiptour.common.entity.place.RegionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionInfoRepository extends JpaRepository<RegionInfo, Integer> {
    Optional<RegionInfo> findByAreaCode(String areaCode);
}
