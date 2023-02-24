package com.knife.example.common.core.feign;

import com.fasterxml.jackson.databind.JavaType;
import com.knife.example.common.core.entity.ResultWrapper;
import com.knife.example.common.core.util.JsonUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * feign响应的解码器
 */
@Slf4j
public class FeignResultDecoder implements Decoder {
 
    private final ObjectFactory<HttpMessageConverters> messageConverters;
 
    public FeignResultDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }
 
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (type instanceof Class
                || type instanceof ParameterizedType
                || type instanceof WildcardType) {
            if (type == ResultWrapper.class) {
                HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(
                        type, this.messageConverters.getObject().getConverters());
                return extractor.extractData(new FeignResponseAdapter(response));
            } else {
                ResultWrapper resultWrapper = (ResultWrapper) decode(response, ResultWrapper.class);
                if (resultWrapper.isSuccess()) {
                    if (type == Object.class) {
                        return resultWrapper.getData();
                    } else {
                        String json = JsonUtil.toJson(resultWrapper.getData());
                        JavaType javaType = JsonUtil.getObjectMapper().getTypeFactory().constructType(type);
                        return JsonUtil.toObject(json, javaType);
                    }
                } else {
                    log.error("失败原因:" + resultWrapper.getMessage());
                    throw new DecodeException(response.status(), resultWrapper.getMessage(), response.request());
                }
            }
        }
        throw new RuntimeException("type is not an instance of Class or ParameterizedType or WildcardType: " + type);
    }
 
    private final class FeignResponseAdapter implements ClientHttpResponse {
 
        private final Response response;
 
        private FeignResponseAdapter(Response response) {
            this.response = response;
        }
 
        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.valueOf(this.response.status());
        }
 
        @Override
        public int getRawStatusCode() {
            return this.response.status();
        }
 
        @Override
        public String getStatusText() {
            return this.response.reason();
        }
 
        @Override
        public void close() {
            try {
                this.response.body().close();
            } catch (IOException ex) {
                // Ignore exception on close...
            }
        }
 
        @Override
        public InputStream getBody() throws IOException {
            return this.response.body().asInputStream();
        }
 
        @Override
        public HttpHeaders getHeaders() {
            return getHttpHeaders(this.response.headers());
        }
    }
 
    private HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return httpHeaders;
    }
}