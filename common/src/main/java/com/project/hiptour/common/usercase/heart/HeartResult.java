package com.project.hiptour.common.usercase.heart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HeartResult {

    private final boolean isSuccess;
    private final String message;
    private final HeartCase heartCase;

    public boolean isHeartInvalidState(){
        return this.heartCase.equals(HeartCase.ALREADY_INACTIVE);
    }
    public boolean isUserNotExisting(){
        return this.heartCase.equals(HeartCase.USER_NOT_EXISTING);
    }
    public boolean isPlaceNotExisting(){
        return this.heartCase.equals(HeartCase.UNKNOWN_PLACE);
    }
    public boolean isHeartNotExisting(){
        return this.heartCase.equals(HeartCase.NOT_EXISTING);
    }
}
