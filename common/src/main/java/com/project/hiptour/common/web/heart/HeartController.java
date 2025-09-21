package com.project.hiptour.common.web.heart;

import com.project.hiptour.common.usercase.heart.HeartResult;
import com.project.hiptour.common.usercase.heart.HeartUserCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/heart")
@AllArgsConstructor
public class HeartController {

    private final HeartUserCase heartUserCase;

    @PostMapping
    public ResponseEntity<HeartResponse> makeHeart(@RequestBody HeartRequest request){

        HeartResult heartResult = this.heartUserCase.makeHeart(request.getUserId(), request.getFeedId());

        return getHeartResponseResponseEntity(request, heartResult);
    }


    @DeleteMapping
    public ResponseEntity<HeartResponse> unHeartHandler(@RequestBody HeartRequest request){

        HeartResult heartResult = this.heartUserCase.unHeart(request.getUserId(), request.getFeedId());

        return getHeartResponseResponseEntity(request, heartResult);

    }

    private ResponseEntity<HeartResponse> getHeartResponseResponseEntity(HeartRequest request, HeartResult heartResult) {
        if(heartResult.isHeartNotExisting()){
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
        }else if(heartResult.isDuplicateHeart()){
            return ResponseEntity.badRequest()
                    .body(new HeartResponse(request, heartResult));
        }
        return ResponseEntity.noContent().build();
    }
}
