package com.charwayh.constant;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.constant
 * @date:2023/5/23
 */
public enum MessageConstant {
    SEND_OK("发送消息成功"),

    CONNECT_UNSUCCESS("连接平台异常"),

    PRODUCER_ABSENT("推送系统填写错误，无此推送系统，请联系管理员"),

    CONSUMER_ABSENT("消费系统填写错误，无此消费系统，请联系管理员"),

    TOPIC_ABSENT("主题填写错误，无此主题，请联系管理员");



    MessageConstant(String description) {
        this.description = description;
    }


    private String description;

    @Override
    public String toString() {
        return description;

    }
}
