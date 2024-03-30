package com.suchtool.nicecommon.core.configuration;

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
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * jackson 配置
 */
@Configuration
public class JacksonConfig {

	@Primary
	@Bean
	public ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder builder,
										JacksonProperties jacksonProperties) {
		ObjectMapper objectMapper = builder.build();

		// 把“忽略重复的模块注册”禁用，否则下面的注册不生效
		objectMapper.disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);

		// 全局配置数字的序列化
		SimpleModule simpleModule = new SimpleModule();
		// 使用String来序列化Long包装类
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		// 使用String来序列化long基本类型
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		// 使用String来序列化BigInteger包装类类型。（有人用BigInteger表示大的整数）
		simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
		// 将使用String来序列化BigDecimal类型
		simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

		objectMapper.registerModule(simpleModule);

		// 全局配置时间的序列化
		objectMapper.registerModule(configTimeModule());

		// 将不匹配的enum转为null，防止报错
		objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

		// 重新设置为生效，避免被其他地方覆盖
		objectMapper.enable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);

		return objectMapper;
	}

	private JavaTimeModule configTimeModule() {
		JavaTimeModule javaTimeModule = new JavaTimeModule();

		String localDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
		String localDateFormat = "yyyy-MM-dd";
		String localTimeFormat = "HH:mm:ss";
		String dateFormat = "yyyy-MM-dd HH:mm:ss";

		// 序列化
		javaTimeModule.addSerializer(
				LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimeFormat)));
		javaTimeModule.addSerializer(
				LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern(localDateFormat)));
		javaTimeModule.addSerializer(
				LocalTime.class,
				new LocalTimeSerializer(DateTimeFormatter.ofPattern(localTimeFormat)));
		javaTimeModule.addSerializer(
				Date.class,
				new DateSerializer(false, new SimpleDateFormat(dateFormat)));

		// 反序列化
		javaTimeModule.addDeserializer(
		        LocalDateTime.class,
		        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(localDateTimeFormat)));
		javaTimeModule.addDeserializer(
		        LocalDate.class,
		        new LocalDateDeserializer(DateTimeFormatter.ofPattern(localDateFormat)));
		javaTimeModule.addDeserializer(
		        LocalTime.class,
		        new LocalTimeDeserializer(DateTimeFormatter.ofPattern(localTimeFormat)));
		javaTimeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
		    @SneakyThrows
		    @Override
		    public Date deserialize(JsonParser jsonParser, DeserializationContext dc) {
		        String text = jsonParser.getText().trim();
		        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		        return sdf.parse(text);
		    }
		});

		return javaTimeModule;
	}
}
