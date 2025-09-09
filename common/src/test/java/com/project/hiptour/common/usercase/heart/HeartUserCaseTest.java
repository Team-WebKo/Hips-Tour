package com.project.hiptour.common.usercase.heart;

import com.project.hiptour.common.entity.heart.Heart;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.place.repository.PlaceRepository;
import com.project.hiptour.common.security.OauthProviderService;
import jakarta.transaction.Transactional;
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


    private Long userId;
    private int placeId;


//    @BeforeEach
//    void init(){
//
//        UserInfo userInfo = UserInfo.builder()
//                .nickName("sample")
//
//                .userIdentifier("kakao-sam")
//                .email("sample@gmail.com")
//                .build();
//
//        Place place = Place.builder()
//                .address1("add")
//                .address2("add2")
//                .placeName("place")
//                .build();
//
//        place.setPlaceId(1000);
//
//        UserInfo userId = this.userRepos.save(userInfo);
//        Place placeId = this.placeRepository.save(place);
//
//        this.userId = userId.getUserId();
//        this.placeId = placeId.getPlaceId();
//    }

    @Test
    @DisplayName("찜하기 API -> 유저와 관광지가 모두 존재하는 경우, 찜하기 API는 성공한다.")
    void t(){
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

        UserInfo userId = this.userRepos.save(userInfo);
        Place placeId = this.placeRepository.save(place);

        HeartResult heartResult = this.userCase.makeHeart(userId.getUserId(), placeId.getPlaceId());
        assertTrue(heartResult.isSuccess());

        List<Heart> byUserIdAndFeedId1 = heartRepos.findByUserUserIdAndFeedPlaceId(userId.getUserId(), placeId.getPlaceId());
        assertNotNull(byUserIdAndFeedId1);
        assertFalse(byUserIdAndFeedId1.isEmpty());
    }

}