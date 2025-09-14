package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.MyReviewResponseDto;

import java.util.List;

public interface MyReviewService {
    List<MyReviewResponseDto> getMyReview(UserInfo userInfo);
}
