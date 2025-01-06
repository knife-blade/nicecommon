package com.suchtool.nicecommon.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
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
import com.suchtool.nicecommon.core.property.NiceCommonGlobalFormatProperty;
import com.suchtool.nicecommon.core.property.NiceCommonJacksonProperty;
import com.suchtool.nicetool.util.lib.datetime.constant.DateTimeFormatConstant;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@ConditionalOnProperty(name = "suchtool.nicecommon.jackson.enable-config", havingValue = "true", matchIfMissing = true)
@Configuration(value = "com.suchtool.nicecommon.niceCommonJacksonConfig", proxyBeanMethods = false)
public class NiceCommonJacksonConfiguration {
    private NiceCommonJacksonProperty jacksonProperty;

    private NiceCommonGlobalFormatProperty globalFormatProperty;

    @Value("${spring.jackson.time-zone:#{null}}")
    private TimeZone timeZone;

    public NiceCommonJacksonConfiguration(NiceCommonJacksonProperty jacksonProperty,
                                          NiceCommonGlobalFormatProperty globalFormatProperty) {
        this.jacksonProperty = jacksonProperty;
        this.globalFormatProperty = globalFormatProperty;
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder,
                                     JacksonProperties jacksonProperties) {
        ObjectMapper objectMapper = builder.build();

        // 把“忽略重复的模块注册”禁用，否则下面的注册不生效
        objectMapper.disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);

        if (globalFormatProperty.getEnableNumberToString()) {
            // 全局配置数字的序列化
            objectMapper.registerModule(configNumberModule());
        }

        // 全局配置日期时间的序列化
        switch (globalFormatProperty.getDateTimeFormatType()) {
            case PRETTY:
                objectMapper.registerModule(configDateTimeModuleByPretty());
                break;
            case TIMESTAMP:
                objectMapper.registerModule(configDateTimeModuleByTimestamp());
                break;
            default:
                break;
        }

        if (jacksonProperty.getEnableAvoidFail()) {
            // 避免报错
            avoidFail(objectMapper);
        }

        // 重新设置为生效，避免被其他地方覆盖
        objectMapper.enable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);

        return objectMapper;
    }

    private void avoidFail(ObjectMapper objectMapper) {
        // 将不匹配的enum转为null，防止报错
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        // 反序列化时，如果json串中存在一些key，但在Java对象中没有，不要报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 序列化时，如果Java对象里没有任何属性，不要报错
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private SimpleModule configNumberModule() {
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

    private JavaTimeModule configDateTimeModuleByPretty() {
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

    private JavaTimeModule configDateTimeModuleByTimestamp() {
        ZoneId zoneId = ZoneId.systemDefault();
        if (timeZone != null) {
            zoneId = timeZone.toZoneId();
        }
        DateTimeTimestampConverter.setZoneId(zoneId);

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 反序列化
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new DateTimeTimestampConverter.LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new DateTimeTimestampConverter.LocalDateDeserializer());

        // 序列化
        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new DateTimeTimestampConverter.LocalDateTimeSerializer());
        javaTimeModule.addSerializer(
                LocalDate.class,
                new DateTimeTimestampConverter.LocalDateSerializer());

        return javaTimeModule;
    }

    public static class DateTimeTimestampConverter {
        private static ZoneId zoneId;

        public static void setZoneId(ZoneId zoneId) {
            DateTimeTimestampConverter.zoneId = zoneId;
        }

        public static ZoneId getZoneId() {
            return DateTimeTimestampConverter.zoneId;
        }

        // 序列化实现
        public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                if (value != null) {
                    long timestamp = value.atZone(getZoneId()).toInstant().toEpochMilli();
                    gen.writeNumber(timestamp);
                }
            }
        }

        // 序列化实现
        public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                if (value != null) {
                    LocalDateTime localDateTime = LocalDateTime.of(value, LocalTime.MIN);
                    long timestamp = localDateTime.atZone(getZoneId()).toInstant().toEpochMilli();
                    gen.writeNumber(timestamp);
                }
            }
        }

        // 反序列化实现
        public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
                    throws IOException {
                long timestamp = p.getValueAsLong();
                if (timestamp > 0) {
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), getZoneId());
                } else {
                    return null;
                }
            }
        }

        // 反序列化实现
        public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
                    throws IOException {
                long timestamp = p.getValueAsLong();
                if (timestamp > 0) {
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), getZoneId()).toLocalDate();
                } else {
                    return null;
                }
            }
        }
    }
}
