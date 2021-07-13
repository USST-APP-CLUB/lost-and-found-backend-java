package com.usst.weapp.lostandfound.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Sunforge
 * @Date 2021-07-13 21:28
 * @Version V1.0.0
 * @Description
 */
@Component
public class TimeUtil {

    private final String format = "yyyy-MM-dd HH:mm:ss";

    public LocalDateTime convertStringToTime(String datetimeString){
        return LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern(format));
    }

    public String convertTimeToString(LocalDateTime datetime){
        return datetime.format(DateTimeFormatter.ofPattern(format));
    }
}
