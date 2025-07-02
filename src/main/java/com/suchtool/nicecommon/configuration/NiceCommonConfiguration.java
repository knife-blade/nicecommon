package com.suchtool.nicecommon.configuration;

import com.suchtool.nicecommon.configuration.inner.NiceCommonMyBatisFillSQLInterceptor;
import com.suchtool.nicecommon.core.advice.NiceCommonGlobalExceptionAdvice;
import com.suchtool.nicecommon.core.advice.NiceCommonGlobalResponseWrapperAdvice;
import com.suchtool.nicecommon.core.aspect.NiceCommonGlobalResponseTraceIdAspect;
import com.suchtool.nicecommon.core.property.*;
import com.suchtool.nicecommon.core.provider.NiceCommonMyBatisFillSQLProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "com.suchtool.nicecommon.niceCommonConfiguration", proxyBeanMethods = false)
public class NiceCommonConfiguration {
    @Bean("com.suchtool.nicecommon.niceCommonGlobalExceptionProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.global-exception")
    public NiceCommonGlobalExceptionProperty niceCommonGlobalExceptionProperty() {
        return new NiceCommonGlobalExceptionProperty();
    }

    @Bean("com.suchtool.nicecommon.niceCommonGlobalResponseWrapperProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.global-response-wrapper")
    public NiceCommonGlobalResponseWrapperProperty niceCommonGlobalResponseWrapperProperty() {
        return new NiceCommonGlobalResponseWrapperProperty();
    }

    @Bean("com.suchtool.nicecommon.niceCommonGlobalResponseTraceIdProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.global-response-trace-id")
    public NiceCommonGlobalResponseTraceIdProperty niceCommonGlobalResponseTraceIdProperty() {
        return new NiceCommonGlobalResponseTraceIdProperty();
    }

    @Bean("com.suchtool.nicecommon.NiceCommonGlobalFormatProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.global-format")
    public NiceCommonGlobalFormatProperty niceCommonGlobalFormatProperty() {
        return new NiceCommonGlobalFormatProperty();
    }

    @Bean("com.suchtool.nicecommon.NiceCommonJacksonProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.jackson")
    public NiceCommonJacksonProperty NiceCommonJacksonProperty() {
        return new NiceCommonJacksonProperty();
    }

    @Bean("com.suchtool.nicecommon.niceCommonGlobalExceptionAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.global-exception.enable", havingValue = "true", matchIfMissing = true)
    public NiceCommonGlobalExceptionAdvice niceCommonGlobalExceptionAdvice(NiceCommonGlobalExceptionProperty niceCommonGlobalExceptionProperty) {
        return new NiceCommonGlobalExceptionAdvice(niceCommonGlobalExceptionProperty);
    }

    @Bean("com.suchtool.nicecommon.niceCommonGlobalResponseWrapperAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.global-response-wrapper.enable", havingValue = "true", matchIfMissing = true)
    public NiceCommonGlobalResponseWrapperAdvice niceCommonGlobalResponseBodyAdvice(NiceCommonGlobalResponseWrapperProperty niceCommonGlobalResponseWrapperProperty) {
        return new NiceCommonGlobalResponseWrapperAdvice(niceCommonGlobalResponseWrapperProperty);
    }

    @Bean("com.suchtool.nicecommon.niceCommonGlobalResponseBodyAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.global-response-trace-id.enable", havingValue = "true", matchIfMissing = true)
    public NiceCommonGlobalResponseTraceIdAspect niceCommonGlobalResponseBodyAdvice(NiceCommonGlobalResponseTraceIdProperty niceCommonGlobalResponseTraceIdProperty) {
        return new NiceCommonGlobalResponseTraceIdAspect(niceCommonGlobalResponseTraceIdProperty);
    }

    @Bean("com.suchtool.nicecommon.niceCommonDateTimeFormPrettyConfig")
    @ConditionalOnExpression("'${suchtool.nicecommon.global-format.date-time-format-type}' == 'pretty'")
    public NiceCommonDateTimeFormPrettyConfiguration niceCommonDateTimeFormPrettyConfig() {
        return new NiceCommonDateTimeFormPrettyConfiguration();
    }

    @ConditionalOnClass(Interceptor.class)
    @ConditionalOnBean(NiceCommonMyBatisFillSQLProvider.class)
    protected static class KafkaAspectConfiguration {
        @Bean("com.suchtool.nicecommon.niceCommonMybatisFillUpdateSqlInterceptor")
        public NiceCommonMyBatisFillSQLInterceptor niceCommonMybatisFillUpdateSqlInterceptor() {
            return new NiceCommonMyBatisFillSQLInterceptor();
        }
    }
}
