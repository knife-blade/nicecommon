package com.suchtool.nicecommon.configuration.inner;

import com.suchtool.nicetool.util.lib.datetime.constant.DateTimeFormatConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration(value = "com.suchtool.nicecommon.niceCommonDateTimeFormPrettyConfig", proxyBeanMethods = false)
public class NiceCommonDateTimeFormPrettyConfig {
    @Bean("com.suchtool.nicecommon.localDateTimeConverter")
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new NiceCommonLocalDateTimeConverter();
    }

    @Bean("com.suchtool.nicecommon.localDateConverter")
    public Converter<String, LocalDate> localDateConverter() {
        return new NiceCommonLocalDateConverter();
    }

    @Bean("com.suchtool.nicecommon.localTimeConverter")
    public Converter<String, LocalTime> localTimeConverter() {
        return new NiceCommonLocalTimeConverter();
    }

    public static class NiceCommonLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String text) {
            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL));
        }
    }

    public static class NiceCommonLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String text) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_FORMAT_NORMAL));
        }
    }

    public static class NiceCommonLocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String text) {
            return LocalTime.parse(text, DateTimeFormatter.ofPattern(DateTimeFormatConstant.TIME_FORMAT_NORMAL));
        }
    }
}