package org.example.onlinemusic.tools;

import lombok.Data;

/**
 * 设置响应数据的格式
 */
@Data
public class ResponseBodyMessage<T>{
    private int status;//状态吗

    private String message;//返回的信息【出错的原因？  没错的原因？】

    private T data;//返回给前端的数据

    public ResponseBodyMessage(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
