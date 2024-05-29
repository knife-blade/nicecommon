package com.suchtool.nicecommon.configuration;

import com.suchtool.nicetool.util.lib.datetime.constant.DateTimeFormatConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration(value = "com.suchtool.nicecommon.localDateTimeConfig", proxyBeanMethods = false)
@ConditionalOnProperty(name = "suchtool.nicecommon.enableFormDateTimeFormat", havingValue = "true", matchIfMissing = true)
public class NiceCommonLocalDateTimeFormConfig {
    @Bean("com.suchtool.nicecommon.localDateTimeConverter")
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new LocalDateTimeConverter();
    }

    @Bean("com.suchtool.nicecommon.localDateConverter")
    public Converter<String, LocalDate> localDateConverter() {
        return new LocalDateConverter();
    }

    @Bean("com.suchtool.nicecommon.localTimeConverter")
    public Converter<String, LocalTime> localTimeConverter() {
        return new LocalTimeConverter();
    }

    public static class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String text) {
            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL));
        }
    }

    public static class LocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String text) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_FORMAT_NORMAL));
        }
    }

    public static class LocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String text) {
            return LocalTime.parse(text, DateTimeFormatter.ofPattern(DateTimeFormatConstant.TIME_FORMAT_NORMAL));
        }
    }
}