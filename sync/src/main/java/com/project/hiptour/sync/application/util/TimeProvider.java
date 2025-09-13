package com.project.hiptour.sync.application.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeProvider {
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
