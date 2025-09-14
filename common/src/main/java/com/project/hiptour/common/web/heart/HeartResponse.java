package com.project.hiptour.common.web.heart;

import com.project.hiptour.common.usercase.heart.HeartResult;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class HeartResponse {

    private long userId;
    private int feedId;
    private String meesage;

    public HeartResponse(HeartRequest req, HeartResult heartResult) {
        this.userId = req.getUserId();
        this.feedId = req.getFeedId();
        this.meesage = heartResult.getMessage();
    }

    public HeartResponse(HeartRequest request, String inactiveState) {
        this.userId = request.getUserId();
        this.feedId = request.getFeedId();
        this.meesage = inactiveState;
    }
}
