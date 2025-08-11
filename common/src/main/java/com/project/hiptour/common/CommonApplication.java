package com.project.hiptour.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.project.hiptour.common",
    "com.project.hiptour.sync.application",
    "com.project.hiptour.sync.controller",
    "com.project.hiptour.sync.domain",
    "com.project.hiptour.sync.global",
    "com.project.hiptour.sync.infrastructure"
})
public class CommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

}
