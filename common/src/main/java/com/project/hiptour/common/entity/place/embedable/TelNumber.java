package com.project.hiptour.common.entity.place.embedable;

import jakarta.persistence.Embeddable;

@Embeddable
public class TelNumber {
    
    private String countryCode;
    private String areaCode;
    
    private String telNumber;

    public TelNumber(String tel) {
        this.countryCode = "82";
        this.areaCode = getAreaCode(tel);
        this.telNumber = getTelNumber(tel);
    }

    private String getTelNumber(String tel) {
        return tel;
    }

    private String getAreaCode(String tel) {
        //TODO :: area code를 가져오는 로직 추가
        return "02";
    }


}
