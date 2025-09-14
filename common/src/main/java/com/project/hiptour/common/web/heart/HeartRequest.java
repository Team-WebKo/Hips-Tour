package com.project.hiptour.common.web.heart;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HeartRequest {
    private long userId;
    private int feedId;
}
