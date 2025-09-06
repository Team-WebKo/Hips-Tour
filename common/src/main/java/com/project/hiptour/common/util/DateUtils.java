package com.project.hiptour.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static LocalDateTime convert(Date date){
        Instant instant = date.toInstant(); // Date → Instant
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
