package com.knife.example.common.core.feign;

import com.fasterxml.jackson.databind.JavaType;
import com.knife.example.common.core.entity.ResultWrapper;
import com.suchtool.nicetool.util.base.JsonUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * feign响应的解码器
 */
@Slf4j
public class FeignResultDecoder extends SpringDecoder {

    public FeignResultDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Response.Body body = response.body();
        String bodyString = StreamUtils.copyToString(body.asInputStream(), StandardCharsets.UTF_8);
        // 这里可以将body打印出来。需要引入nicelog
        // NiceLogFeignContextThreadLocal.saveOriginFeignResponseBody(bodyString);

        // body流只能读一次，必须重新封装一下
        Response newResponse = response.toBuilder().body(bodyString, StandardCharsets.UTF_8).build();

        if (type == ResultWrapper.class) {
            return super.decode(newResponse, type);
        } else {
            ResultWrapper resultWrapper = (ResultWrapper) decode(newResponse, ResultWrapper.class);
            if (resultWrapper.getSuccess()) {
                if (type == Object.class) {
                    return resultWrapper.getData();
                } else {
                    String json = JsonUtil.toJsonString(resultWrapper.getData());
                    JavaType javaType = JsonUtil.getObjectMapper().getTypeFactory().constructType(type);
                    return JsonUtil.toObject(json, javaType);
                }
            } else {
                log.error("失败原因:" + resultWrapper.getMessage());
                throw new DecodeException(newResponse.status(), resultWrapper.getMessage(), newResponse.request());
            }
        }
    }
}