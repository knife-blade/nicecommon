package com.suchtool.nicecommon.configuration;

import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@ConditionalOnExpression("'${suchtool.nicecommon.global-format.date-time-format-type}' == 'timestamp'")
@Configuration(value = "com.suchtool.nicecommon.niceCommonDateTimeFormTimestampConfig", proxyBeanMethods = false)
public class NiceCommonDateTimeFormTimestampConfiguration {
    private static ZoneId zoneId = readZoneId();

    @Bean
    public Converter<Long, LocalDateTime> timestampLongToLocalDateTimeConverter() {
        return new TimestampLongToLocalDateTimeConverter();
    }
    @Bean
    public Converter<String, LocalDateTime> timestampStringToLocalDateTimeConverter() {
        return new TimestampStringToLocalDateTimeConverter();
    }
    @Bean
    public Converter<Long, LocalDate> timestampLongToLocalDateConverter() {
        return new TimestampLongToLocalDateConverter();
    }
    @Bean
    public Converter<String, LocalDate> timestampStringToLocalDateConverter() {
        return new TimestampStringToLocalDateConverter();
    }

    public static class TimestampLongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
        @Override
        public LocalDateTime convert(@NonNull Long timestamp) {
            // 如果timestamp是null，则不会执行到此方法
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        }
    }
    public static class TimestampStringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(@NonNull String timestamp) {
            // 如果timestamp是null，则不会执行到此方法
            if (!StringUtils.hasText(timestamp)) {
                return null;
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(timestamp)), zoneId);
        }
    }
    public static class TimestampLongToLocalDateConverter implements Converter<Long, LocalDate> {
        @Override
        public LocalDate convert(@NonNull Long timestamp) {
            // 如果timestamp是null，则不会执行到此方法
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId).toLocalDate();
        }
    }
    public static class TimestampStringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(@NonNull String timestamp) {
            // 如果timestamp是null，则不会执行到此方法
            if (!StringUtils.hasText(timestamp)) {
                return null;
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(timestamp)), zoneId).toLocalDate();
        }
    }

    private static ZoneId readZoneId() {
        if (zoneId == null) {
            TimeZone timeZone = ApplicationContextHolder.getContext().getEnvironment()
                    .getProperty("spring.jackson.time-zone", TimeZone.class, null);
            zoneId = timeZone.toZoneId();
        }

        return zoneId;
    }
}