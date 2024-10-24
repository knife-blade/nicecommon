package com.suchtool.nicecommon.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.suchtool.nicecommon.core.advice.GlobalExceptionAdvice;
import com.suchtool.nicecommon.core.advice.GlobalResponseBodyAdvice;
import com.suchtool.nicecommon.core.property.NiceCommonAdviceProperty;
import com.suchtool.nicetool.util.lib.datetime.constant.DateTimeFormatConstant;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration(value = "com.suchtool.nicecommon.niceCommonConfiguration", proxyBeanMethods = false)
public class NiceCommonConfiguration {
    @Bean("com.suchtool.nicecommon.niceCommonAdviceProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.advice")
    public NiceCommonAdviceProperty niceCommonAdviceProperty() {
        return new NiceCommonAdviceProperty();
    }

    @Bean("com.suchtool.nicecommon.globalExceptionAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.advice.enableGlobalExceptionAdvice", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionAdvice globalExceptionAdvice(NiceCommonAdviceProperty property) {
        return new GlobalExceptionAdvice(
                property.getGlobalExceptionAdvice(),
                property.getEnableGlobalExceptionAdviceLog());
    }

    @Bean("com.suchtool.nicecommon.globalResponseBodyAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.advice.enableGlobalResponseBodyAdvice", havingValue = "true", matchIfMissing = true)
    public GlobalResponseBodyAdvice globalResponseBodyAdvice(NiceCommonAdviceProperty property) {
        return new GlobalResponseBodyAdvice(property.getGlobalResponseAdvice());
    }

}
