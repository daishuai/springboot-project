package com.daishuai.websocket.client.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author Daishuai
 * @description 自定义消息转换器
 * @date 2019/8/9 17:44
 */
public class KemMessageConverter extends AbstractMessageConverter {
    
    /**
     * with fastJson config
     */
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    
    public KemMessageConverter(MimeType... supportedMimeTypes) {
        super(Arrays.asList(supportedMimeTypes));
    }
    
    /**
     * @return the fastJsonConfig.
     * @since 1.2.47
     */
    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }
    
    /**
     * @param fastJsonConfig the fastJsonConfig to set.
     * @since 1.2.47
     */
    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }
    
    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }
    
    @Override
    protected boolean canConvertFrom(Message<?> message, Class<?> targetClass) {
        return supports(targetClass);
    }
    
    @Override
    protected boolean canConvertTo(Object payload, MessageHeaders headers) {
        return supports(payload.getClass());
    }
    
    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        // parse byte[] or String payload to Java Object
        Object payload = message.getPayload();
        Object obj = null;
        if (payload instanceof byte[]) {
            obj = JSON.parseObject((byte[]) payload, 0, ((byte[]) payload).length,
                    fastJsonConfig.getCharset(), targetClass, fastJsonConfig.getFeatures());
        } else if (payload instanceof String) {
            obj = JSON.parseObject((String) payload, targetClass, fastJsonConfig.getFeatures());
        }
        
        return obj;
    }
    
    @Override
    protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        String jsonString = JSON.toJSONString(payload, fastJsonConfig.getSerializeConfig(),
                fastJsonConfig.getSerializeFilters(), fastJsonConfig.getSerializerFeatures());
        // encode payload to json string
        try {
            return jsonString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return jsonString;
        }
    }
}
