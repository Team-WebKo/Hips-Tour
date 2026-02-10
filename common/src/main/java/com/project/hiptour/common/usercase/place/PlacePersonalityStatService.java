package com.project.hiptour.common.usercase.place;

import com.project.hiptour.common.entity.perference.DimensionType;
import com.project.hiptour.common.entity.perference.OceanMaster;
import com.project.hiptour.common.entity.perference.PlacePersonalityStat;
import com.project.hiptour.common.entity.perference.ReviewSelections;
import com.project.hiptour.common.entity.perference.repos.PlacePersonalityStatRepository;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewSelectionsRepository; // 경로는 프로젝트에 맞게 확인 필요
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserPersonalityStat;
import com.project.hiptour.common.entity.users.repos.UserPersonalityStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlacePersonalityStatService {
    private final PlacePersonalityStatRepository placePersonalityStatRepository;
    private final UserPersonalityStatRepository userPersonalityStatRepository;
    private final ReviewSelectionsRepository reviewSelectionsRepository;

    private static final BigDecimal DEFENCE_FACTOR = new BigDecimal("0.4");

    @Transactional
    public void calculateAndApplyPlacePersonality(Review review) {
        Place place = review.getPlace();
        UserInfo user = review.getUserInfo();
        if (place == null || user == null) {
            return;
        }

        // 1. 사용자 통계(UserPersonalityStat) 확인: 사용자가 추천 비활성이면 계산 중단
        Optional<UserPersonalityStat> userStatOptional = userPersonalityStatRepository.findByUserInfo(user);
        if (userStatOptional.isEmpty()) {
            return;
        }
        UserPersonalityStat userStat = userStatOptional.get();

        PlacePersonalityStat placeStat = placePersonalityStatRepository.findByPlace(place).get();
        

        List<ReviewSelections> selections = reviewSelectionsRepository.findByReview(review);
        if (selections.isEmpty()) {
            return;
        }

        Map<DimensionType, List<OceanMaster>> selectionsByDimension = selections.stream()
                .map(ReviewSelections::getOceanMaster)
                .collect(Collectors.groupingBy(OceanMaster::getTargetDimension));

        for (Map.Entry<DimensionType, List<OceanMaster>> entry : selectionsByDimension.entrySet()) {
            DimensionType dimension = entry.getKey();
            List<OceanMaster> masters = entry.getValue();
            
            BigDecimal p_init = getScoreByDimension(placeStat, dimension);
            BigDecimal userScore = getScoreByDimension(userStat, dimension);
            int totalCount_old = getCountByDimension(placeStat, dimension, "total");
            int positiveCount_old = getCountByDimension(placeStat, dimension, "positive");

            double k_adjustment = 0.0;
            int positiveIncrement = 0;

            for (OceanMaster master : masters) {
                if (master.getPolarityWeight().compareTo(BigDecimal.ZERO) > 0) {
                    positiveIncrement++;
                } else if (master.getPolarityWeight().compareTo(BigDecimal.ZERO) < 0 && p_init.compareTo(userScore) < 0) {
                    k_adjustment += DEFENCE_FACTOR.doubleValue();
                }
            }

            int totalCountNew = totalCount_old + masters.size();
            int positiveCountNew = positiveCount_old + positiveIncrement;
            if (totalCountNew == 0) continue;

            double k = (double) positiveCountNew + k_adjustment;
            double w = Math.min((double) totalCountNew / 50.0, 1.0);

            double pn_raw = (1.0 - w) * p_init.doubleValue() + w * (k / (double) totalCountNew * 10.0);
            BigDecimal pn = new BigDecimal(pn_raw).setScale(3, RoundingMode.HALF_UP);

            updatePlaceStat(placeStat, dimension, pn, totalCountNew, positiveCountNew);

            placePersonalityStatRepository.save(placeStat);
        }
    }

    private BigDecimal getScoreByDimension(Object stat, DimensionType dimension) {
        BigDecimal score;
        if (stat instanceof PlacePersonalityStat) {
            score = switch (dimension) {
                case O -> ((PlacePersonalityStat) stat).getScoreO();
                case C -> ((PlacePersonalityStat) stat).getScoreC();
                case E -> ((PlacePersonalityStat) stat).getScoreE();
                case A -> ((PlacePersonalityStat) stat).getScoreA();
                case N -> ((PlacePersonalityStat) stat).getScoreN();
            };
        } else if (stat instanceof UserPersonalityStat) {
            score = switch (dimension) {
                case O -> ((UserPersonalityStat) stat).getScoreO();
                case C -> ((UserPersonalityStat) stat).getScoreC();
                case E -> ((UserPersonalityStat) stat).getScoreE();
                case A -> ((UserPersonalityStat) stat).getScoreA();
                case N -> ((UserPersonalityStat) stat).getScoreN();
            };
        } else {
             score = BigDecimal.valueOf(5.0);
        }
        return Optional.ofNullable(score).orElse(BigDecimal.valueOf(5.0));
    }

    private int getCountByDimension(PlacePersonalityStat stat, DimensionType dimension, String type) {
        Integer count;
        switch (dimension) {
            case O:
                count = type.equals("total") ? stat.getTotalCountO() : stat.getPositiveCountO();
                break;
            case C:
                count = type.equals("total") ? stat.getTotalCountC() : stat.getPositiveCountC();
                break;
            case E:
                count = type.equals("total") ? stat.getTotalCountE() : stat.getPositiveCountE();
                break;
            case A:
                count = type.equals("total") ? stat.getTotalCountA() : stat.getPositiveCountA();
                break;
            case N:
                count = type.equals("total") ? stat.getTotalCountN() : stat.getPositiveCountN();
                break;
            default:
                count = 0;
        }
        return Optional.ofNullable(count).orElse(0);
    }

    private void updatePlaceStat(PlacePersonalityStat stat, DimensionType dimension, BigDecimal newScore, int newTotalCount, int newPositiveCount) {
        switch (dimension) {
            case O:
                stat.setScoreO(newScore);
                stat.setTotalCountO(newTotalCount);
                stat.setPositiveCountO(newPositiveCount);
                break;
            case C:
                stat.setScoreC(newScore);
                stat.setTotalCountC(newTotalCount);
                stat.setPositiveCountC(newPositiveCount);
                break;
            case E:
                stat.setScoreE(newScore);
                stat.setTotalCountE(newTotalCount);
                stat.setPositiveCountE(newPositiveCount);
                break;
            case A:
                stat.setScoreA(newScore);
                stat.setTotalCountA(newTotalCount);
                stat.setPositiveCountA(newPositiveCount);
                break;
            case N:
                stat.setScoreN(newScore);
                stat.setTotalCountN(newTotalCount);
                stat.setPositiveCountN(newPositiveCount);
                break;
        }
    }
}
