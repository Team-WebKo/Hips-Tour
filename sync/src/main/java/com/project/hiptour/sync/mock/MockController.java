package com.project.hiptour.sync.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
public class MockController {

    @GetMapping("/tourapi/")
    public String getTourApi(String serviceId){
        return "{}";
    }


}
