package com.project.hiptour.common.web.heart;

import com.project.hiptour.common.usercase.heart.HeartResult;
import com.project.hiptour.common.usercase.heart.HeartUserCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/heart")
@AllArgsConstructor
public class HeartController {

    private final HeartUserCase heartUserCase;

    @PostMapping
    public ResponseEntity<HeartResponse> makeHeart(@RequestBody HeartRequest request){

        HeartResult heartResult = this.heartUserCase.makeHeart(request.getUserId(), request.getFeedId());
        if(heartResult.isSuccess()){
            return ResponseEntity.noContent().build();
        }else if(heartResult.isHeartNotExisting()){
            return ResponseEntity
                    .notFound()
                    .build();
        }else if(heartResult.isUserNotExisting()){
            return ResponseEntity
                    .badRequest()
                    .body(new HeartResponse(request, heartResult));
        }else if(heartResult.isPlaceNotExisting()){
            return ResponseEntity
                    .badRequest()
                    .body(new HeartResponse(request, heartResult));
        }else if(heartResult.isHeartInvalidState()){
            return ResponseEntity
                    .internalServerError()
                    .body(new HeartResponse(request, "inactive state"));
        }
    }
}
