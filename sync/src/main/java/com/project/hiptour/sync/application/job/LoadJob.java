package com.project.hiptour.sync.application.job;

import com.project.hiptour.sync.domain.LoadJobStatus;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class LoadJob {
    private final int dailyApiCallLimit;
    private int apiCallCount;

    private String currentAreaCode;
    private int currentPageNo;
    private boolean areaCompleted;

    public LoadJob(int dailyApiCallLimit, Optional<LoadJobStatus> status, List<String> areaCode) {
        this.dailyApiCallLimit = dailyApiCallLimit;
        this.apiCallCount = 0;
        this.areaCompleted = false;

        status.ifPresentOrElse(
                s -> {
                    this.currentAreaCode = s.getLastSucceededAreaCode();
                    this.currentPageNo = s.getLastSucceededPageNo() + 1;
                },
                () -> {
                    this.currentAreaCode = areaCode.get(0);
                    this.currentPageNo = 1;
                }
        );
    }

    public boolean isApiLimitReached() {
        return this.apiCallCount >= this.dailyApiCallLimit;
    }

    public void incrementApiCallCount() {
        this.apiCallCount++;
    }

    public void advanceToNextPage() {
        this.currentPageNo++;
    }

    public void startArea(String areaCode) {
        this.currentAreaCode = areaCode;
        this.currentPageNo = 1;
        this.areaCompleted = false;
    }

    public void markAreaAsCompleted() {
        this.areaCompleted = true;
    }

    public int getLastSucceededPageNo() {
        return Math.max(0, this.currentPageNo - 1);
    }
}
