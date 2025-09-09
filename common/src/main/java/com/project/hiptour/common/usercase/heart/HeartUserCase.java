package com.project.hiptour.common.usercase.heart;

import com.project.hiptour.common.entity.heart.Heart;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.place.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HeartUserCase {

    private final HeartRepos heartRepos;
    private final UserRepos userRepos;
    private final PlaceRepository placeRepository;
    /**
     * @apiNote <p>
     *
     *     1. 사용자는 관광지에 대하여 찜할 수 있다.
     *     2. 사용자는 이미 찜한 관광지에 대하여 찜을 해제할 수 있다.
     *        - 찜하지 않은 관광지를 찜 해제할 수는 없다.
     * </p>
     *
     * **/
    @Transactional(Transactional.TxType.SUPPORTS)
    public HeartResult makeHeart(long userId, int feedId){

        Optional<UserInfo> userInfo = this.userRepos.findById(userId);
        Optional<Place> placeId = this.placeRepository.findById(feedId);

        if(userInfo.isEmpty()){
            return new HeartResult(false, "unknown user information");
        }

        if(placeId.isEmpty()){
            return new HeartResult(false, "unknown place information");
        }

        UserInfo ui = userInfo.get();
        Place place = placeId.get();

        Heart heart = Heart.builder()
                .feed(place)
                .user(ui)
                .isActive(true)
                .build();

        this.heartRepos.save(heart);
        return new HeartResult(true, "success");

    }

//    @Transactional
//    public HeartResult unHeart(long userId, int feedId){
//
////        Heart byUserIdAndFeedId = this.heartRepos.findByUserIdAndFeedId(userId, feedId);
////        if(byUserIdAndFeedId == null || !byUserIdAndFeedId.isActive()){
////            return new HeartResult(false, "invalid state!");
////        }
////
////        byUserIdAndFeedId.setActive(false);
////        byUserIdAndFeedId.setUpdatedAt(LocalDateTime.now());
////
////        return new HeartResult(true, "successfully unmarked");
//
//    }
//

}
