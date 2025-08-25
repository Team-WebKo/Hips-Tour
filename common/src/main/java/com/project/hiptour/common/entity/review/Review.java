package com.project.hiptour.common.entity.review;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place placeId;
    
    //TODO :: text 길이 제한에 대한 내용은 후에 검사 후 처리해야 할 듯
    private String headText;
    private String bodyText;
    @ElementCollection
    @CollectionTable(name = "hashtags",
            joinColumns = @JoinColumn(name = "hashtag"))
    private List<HashTag> hashTags = new ArrayList<>();


}
