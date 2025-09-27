package com.project.hiptour.common.usercase.heart;

import com.project.hiptour.common.entity.heart.Heart;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.project.hiptour.common.usercase.heart.HeartCase.DUPLICATE_HEART;
import static com.project.hiptour.common.usercase.heart.HeartCase.USER_NOT_EXISTING;

@Slf4j
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
            log.warn("this user {} is not existing", userId);
            return new HeartResult(false, "unknown user information", USER_NOT_EXISTING);
        }

        if(placeId.isEmpty()){
            log.warn("this place {} is not existing", feedId);
            return new HeartResult(false, "unknown place information", USER_NOT_EXISTING);
        }

        UserInfo ui = userInfo.get();
        Place place = placeId.get();

        List<Heart> heartLists = this.heartRepos.findByUserUserIdAndFeedPlaceId(ui.getUserId(), place.getPlaceId());
        if(!heartLists.isEmpty()){
            return new HeartResult(false, "already marked", DUPLICATE_HEART);
        }

        Heart heart = Heart.builder()
                .feed(place)
                .user(ui)
                .isActive(true)
                .build();

        this.heartRepos.save(heart);

        log.debug("successfully updated heart!!");

        return new HeartResult(true, "success", HeartCase.SUCCESS);

    }

    @Transactional
    public HeartResult unHeart(long userId, int feedId){

        log.debug("unHeart request came with id");

        List<Heart> heartList = this.heartRepos.findByUserUserIdAndFeedPlaceId(userId, feedId);

        if(heartList.size() != 1){
            log.warn("this un-heart request contains invalid id key!");
            return new HeartResult(false, "invalid state!! the id is not existing", HeartCase.NOT_EXISTING);
        }else{
            Heart heart = heartList.get(0);
            if(!heart.isActive()){
                log.debug("the state is already inactive!! {}", heart);
                return new HeartResult(false, "invalid state!! the id is already inactive", HeartCase.ALREADY_INACTIVE);
            }
            //TODO :: 로직 수정하기.

            this.heartRepos.delete(heart);
            return new HeartResult(true, "successfully unmarked", HeartCase.SUCCESS);

        }
    }


}
