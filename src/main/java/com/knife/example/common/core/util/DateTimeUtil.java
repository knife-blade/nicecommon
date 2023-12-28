package com.knife.example.common.core.util;

import com.knife.example.common.core.constant.DateTimeFormatConstant;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    /**
     * 格式化
     *
     * @param localDateTime 时间
     * @param format        格式。见{@link DateTimeFormatConstant}
     */
    public static String format(LocalDateTime localDateTime,
                                String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(LocalDate localDate,
                                String format) {
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(LocalTime localTime,
                                String format) {
        return localTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime parseToLocalDateTime(String localDateTimeString,
                                                     String format) {
        return LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate parseToLocalDate(String localDateString,
                                             String format) {
        return LocalDate.parse(localDateString, DateTimeFormatter.ofPattern(format));
    }

    public static LocalTime parseToLocalTime(String localTimeString,
                                             String format) {
        return LocalTime.parse(localTimeString, DateTimeFormatter.ofPattern(format));
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

    // public static void main(String[] args) {
    //     String format = format(LocalDateTime.now(), DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL);
    //     System.out.println(format);
    // }
}
