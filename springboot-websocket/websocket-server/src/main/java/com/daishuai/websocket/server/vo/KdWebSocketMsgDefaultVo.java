package com.daishuai.websocket.server.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Daishuai
 * @description WebSocket请求Vo
 * @date 2019/8/10 11:06
 */
@Data
@Builder
public class KdWebSocketMsgDefaultVo extends KdWebSocketMsgAbsVo<String, String, String> {

    private String clientId;

    private String userId;

    private String destination;

    private String payload;



    @Override
    public int getPayloadLength() {
        return StringUtils.isBlank(getPayload()) ? getPayload().length() : 0;
    }

    @Override
    public String toString() {
        return "KdWebSocketMsgDefaultVo{" +
                "userId=" + clientId +
                "userId=" + userId +
                ", destination=" + destination +
                ", payload=" + payload +
                '}';
    }
}
