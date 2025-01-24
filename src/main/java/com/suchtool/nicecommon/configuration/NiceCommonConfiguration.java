package com.suchtool.nicecommon.configuration;

import com.suchtool.nicecommon.core.advice.NiceCommonGlobalExceptionAdvice;
import com.suchtool.nicecommon.core.advice.NiceCommonGlobalResponseBodyAdvice;
import com.suchtool.nicecommon.core.property.NiceCommonGlobalExceptionProperty;
import com.suchtool.nicecommon.core.property.NiceCommonGlobalFormatProperty;
import com.suchtool.nicecommon.core.property.NiceCommonGlobalResponseProperty;
import com.suchtool.nicecommon.core.property.NiceCommonJacksonProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

    @Bean("com.suchtool.nicecommon.niceCommonGlobalResponseProperty")
    @ConfigurationProperties(prefix = "suchtool.nicecommon.global-response")
    public NiceCommonGlobalResponseProperty niceCommonGlobalResponseProperty() {
        return new NiceCommonGlobalResponseProperty();
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

    @Bean("com.suchtool.nicecommon.niceCommonGlobalResponseBodyAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.global-response.enable", havingValue = "true", matchIfMissing = true)
    public NiceCommonGlobalResponseBodyAdvice niceCommonGlobalResponseBodyAdvice(NiceCommonGlobalResponseProperty niceCommonGlobalResponseProperty) {
        return new NiceCommonGlobalResponseBodyAdvice(niceCommonGlobalResponseProperty);
    }

    @Bean("com.suchtool.nicecommon.niceCommonDateTimeFormPrettyConfig")
    @ConditionalOnExpression("'${suchtool.nicecommon.global-format.date-time-format-type}' == 'pretty'")
    public NiceCommonDateTimeFormPrettyConfiguration niceCommonDateTimeFormPrettyConfig() {
        return new NiceCommonDateTimeFormPrettyConfiguration();
    }

    @Bean("com.suchtool.nicecommon.niceCommonMybatisFillUpdateSqlInterceptor")
    @ConditionalOnBean()
    public NiceCommonMyBatisFillSQLInterceptor niceCommonMybatisFillUpdateSqlInterceptor() {
        return new NiceCommonMyBatisFillSQLInterceptor();
    }
}
