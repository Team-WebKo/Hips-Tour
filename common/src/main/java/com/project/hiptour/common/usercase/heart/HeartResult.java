package com.project.hiptour.common.usercase.heart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HeartResult {
    private final boolean isSuccess;
    private final String message;
    private final HeartCase heartCase;
}
