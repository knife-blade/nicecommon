package com.knife.example.common.core.util;
 
import com.knife.example.common.core.constant.DateTimeConstant;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
 
public class DateTimeUtil {
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern(DateTimeConstant.DATE_TIME_FORMAT_NORMAL);
 
    public static String toNormalString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(
                DateTimeConstant.DATE_TIME_FORMAT_NORMAL));
    }
 
    public static String toNormalString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(
                DateTimeConstant.DATE_FORMAT_NORMAL));
    }
 
    public static String toNormalString(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(
                DateTimeConstant.TIME_FORMAT_NORMAL));
    }
 
    public static LocalDateTime normalStringToLocalDateTime(String localDateTimeString) {
        return LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ofPattern(
                DateTimeConstant.DATE_TIME_FORMAT_NORMAL));
    }
 
    public static LocalDate normalStringToLocalDate(String localDateString) {
        return LocalDate.parse(localDateString, DateTimeFormatter.ofPattern(
                DateTimeConstant.DATE_FORMAT_NORMAL));
    }
 
    public static LocalTime normalStringToLocalTime(String localTimeString) {
        return LocalTime.parse(localTimeString, DateTimeFormatter.ofPattern(
                DateTimeConstant.TIME_FORMAT_NORMAL));
    }
 
    public static LocalDateTime toLocalLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
 
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
 
    public static LocalTime toLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
 
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
 
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
 
    public static Long toTimeStamp(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
 
    public static Long toTimeStamp(LocalDate localDate) {
        return localDate
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
    }
 
    public static LocalDateTime toLocalDateTime(Long timeStamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timeStamp),
                ZoneId.systemDefault());
    }
 
    public static LocalDate toLocalDate(Long timeStamp) {
        return LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(timeStamp),
                        ZoneId.systemDefault()
                )
                .toLocalDate();
    }
}