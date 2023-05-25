package com.charwayh.constant;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.constant
 * @date:2023/5/23
 */
public enum MessageConstant {
    SEND_OK("发送消息成功"),

    CONNECT_UNSUCCESS("连接平台异常");


    MessageConstant(String description) {
        this.description = description;
    }


    private String description;

    @Override
    public String toString() {
        return description;

    }
}
