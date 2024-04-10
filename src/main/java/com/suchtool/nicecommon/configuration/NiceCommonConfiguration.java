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
        return new GlobalExceptionAdvice(property.getGlobalExceptionAdvice());
    }

    @Bean("com.suchtool.nicecommon.globalResponseBodyAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.advice.enableGlobalResponseBodyAdvice", havingValue = "true", matchIfMissing = true)
    public GlobalResponseBodyAdvice globalResponseBodyAdvice(NiceCommonAdviceProperty property) {
        return new GlobalResponseBodyAdvice(property.getGlobalResponseAdvice());
    }

    @Configuration(value = "com.suchtool.nicecommon.jacksonConfig",proxyBeanMethods = false)
    @ConditionalOnProperty(name = "suchtool.nicecommon.enableJacksonConfig", havingValue = "true", matchIfMissing = true)
    protected static class JacksonConfig {
        @Primary
        @Bean
        public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder,
                                         JacksonProperties jacksonProperties) {
            ObjectMapper objectMapper = builder.build();

            // 把“忽略重复的模块注册”禁用，否则下面的注册不生效
            objectMapper.disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);

            // 全局配置数字的序列化
            objectMapper.registerModule(configSimpleModule());

            // 全局配置时间的序列化
            objectMapper.registerModule(configTimeModule());

            // 将不匹配的enum转为null，防止报错
            objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

            // 重新设置为生效，避免被其他地方覆盖
            objectMapper.enable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);

            return objectMapper;
        }

        private SimpleModule configSimpleModule() {
            SimpleModule simpleModule = new SimpleModule();
            // 使用String来序列化Long包装类
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            // 使用String来序列化long基本类型
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            // 使用String来序列化BigInteger包装类类型。（有人用BigInteger表示大的整数）
            simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
            // 将使用String来序列化BigDecimal类型
            simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

            return simpleModule;
        }

        private JavaTimeModule configTimeModule() {
            JavaTimeModule javaTimeModule = new JavaTimeModule();

            // 序列化
            javaTimeModule.addSerializer(
                    LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL)));
            javaTimeModule.addSerializer(
                    LocalDate.class,
                    new LocalDateSerializer(DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_FORMAT_NORMAL)));
            javaTimeModule.addSerializer(
                    LocalTime.class,
                    new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateTimeFormatConstant.TIME_FORMAT_NORMAL)));
            javaTimeModule.addSerializer(
                    Date.class,
                    new DateSerializer(false, new SimpleDateFormat(DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL)));

            // 反序列化
            javaTimeModule.addDeserializer(
                    LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL)));
            javaTimeModule.addDeserializer(
                    LocalDate.class,
                    new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatConstant.DATE_FORMAT_NORMAL)));
            javaTimeModule.addDeserializer(
                    LocalTime.class,
                    new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatConstant.TIME_FORMAT_NORMAL)));
            javaTimeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
                @SneakyThrows
                @Override
                public Date deserialize(JsonParser jsonParser, DeserializationContext dc) {
                    String text = jsonParser.getText().trim();
                    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormatConstant.DATE_TIME_FORMAT_NORMAL);
                    return sdf.parse(text);
                }
            });

            return javaTimeModule;
        }
    }

    @Configuration(value = "com.suchtool.nicecommon.localDateTimeConfig",proxyBeanMethods = false)
    @ConditionalOnProperty(name = "suchtool.nicecommon.enableFormDateTimeFormat", havingValue = "true", matchIfMissing = true)
    public static class LocalDateTimeConfig {
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

}
