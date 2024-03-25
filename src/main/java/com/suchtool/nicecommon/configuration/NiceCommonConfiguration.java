package com.suchtool.nicecommon.configuration;

import com.suchtool.nicecommon.core.advice.GlobalExceptionAdvice;
import com.suchtool.nicecommon.core.advice.GlobalResponseBodyAdvice;
import com.suchtool.nicecommon.core.configuration.JacksonConfig;
import com.suchtool.nicecommon.core.property.NiceCommonAdviceProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

    @Bean("com.suchtool.nicecommon.jacksonConfig")
    @ConditionalOnProperty(name = "suchtool.nicecommon.enableJacksonConfig", havingValue = "true", matchIfMissing = true)
    public JacksonConfig jacksonConfig() {
        return new JacksonConfig();
    }
}
