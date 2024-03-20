package com.suchtool.nicecommon.configuration;

import com.suchtool.nicecommon.core.advice.GlobalExceptionAdvice;
import com.suchtool.nicecommon.core.advice.GlobalResponseBodyAdvice;
import com.suchtool.nicecommon.core.configuration.JacksonConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NiceCommonConfiguration {
    @Bean("com.suchtool.nicecommon.globalExceptionAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.enableGlobalExceptionAdvice", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionAdvice globalExceptionAdvice() {
        return new GlobalExceptionAdvice();
    }

    @Bean("com.suchtool.nicecommon.globalResponseBodyAdvice")
    @ConditionalOnProperty(name = "suchtool.nicecommon.enableGlobalResponseBodyAdvice", havingValue = "true", matchIfMissing = true)
    public GlobalResponseBodyAdvice globalResponseBodyAdvice() {
        return new GlobalResponseBodyAdvice();
    }

    @Bean("com.suchtool.nicecommon.jacksonConfig")
    @ConditionalOnProperty(name = "suchtool.nicecommon.enableJacksonConfig", havingValue = "true", matchIfMissing = true)
    public JacksonConfig jacksonConfig() {
        return new JacksonConfig();
    }
}
