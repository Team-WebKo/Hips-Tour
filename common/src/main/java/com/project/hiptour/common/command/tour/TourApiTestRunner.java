package com.project.hiptour.common.command.tour;

import jakarta.annotation.PostConstruct;

public class TourApiTestRunner {
    private final TourApiClient tourApiClient;

    public TourApiTestRunner(TourApiClient tourApiClient) {
        this.tourApiClient = tourApiClient;
    }

    @PostConstruct
    public void run() {
        String result = tourApiClient.fetchAreaCodeJson(1, 10);
        System.out.println(result);
    }
}
