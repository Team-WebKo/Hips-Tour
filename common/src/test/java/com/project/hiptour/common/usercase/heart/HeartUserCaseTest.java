package com.project.hiptour.common.usercase.heart;

import com.project.hiptour.common.entity.heart.Heart;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.security.OauthProviderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class HeartUserCaseTest {

    @MockBean
    OauthProviderService service;
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private HeartUserCase userCase;
    @Autowired
    private HeartRepos heartRepos;


    private UserInfo userInfo;
    private Place place;


    @BeforeEach
    void init(){
        UserInfo userInfo = UserInfo.builder()
                .nickName(UUID.randomUUID().toString())
                .userIdentifier(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .build();

        Place place = Place.builder()
                .address1(UUID.randomUUID().toString())
                .address2(UUID.randomUUID().toString())
                .placeName(UUID.randomUUID().toString())
                .geoPoint(new GeoPoint())
                .build();

        this.userInfo = this.userRepos.save(userInfo);
        this.place = this.placeRepository.save(place);
    }

    @Test
    @DisplayName("찜하기 API -> 유저와 관광지가 모두 존재하는 경우, 찜하기 API는 성공한다.")
    void t(){

        HeartResult heartResult = this.userCase.makeHeart(this.userInfo.getUserId(), this.place.getPlaceId());
        assertTrue(heartResult.isSuccess());

        List<Heart> byUserIdAndFeedId1 = heartRepos.findByUserUserIdAndFeedPlaceId(this.userInfo.getUserId(), this.place.getPlaceId());
        assertNotNull(byUserIdAndFeedId1);
        assertFalse(byUserIdAndFeedId1.isEmpty());
    }

    @Test
    @DisplayName("찜하기가 존재하는 경우, 상태를 변경한다")
    void t2(){

        Heart heart = Heart.builder()
                .user(this.userInfo)
                .feed(this.place)
                .isActive(true)
                .build();

        Heart savedHeart = this.heartRepos.save(heart);

        HeartResult heartResult = this.userCase.unHeart(savedHeart.getId());
        assertTrue(heartResult.isSuccess());

        Heart unHearted= this.heartRepos.findById(savedHeart.getId()).get();
        assertFalse(unHearted.isActive());

    }

}