package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.reviews.service.CreateReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final CreateReviewService createReviewService;

}
