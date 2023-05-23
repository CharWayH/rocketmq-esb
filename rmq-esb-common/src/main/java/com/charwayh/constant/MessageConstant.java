package com.charwayh.constant;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.constant
 * @date:2023/5/23
 */
public enum MessageConstant {
    SEND_SUCCESS("发送消息成功");


    MessageConstant(String description) {

        this.description = description;
    }


    private String description;

    @Override
    public String toString() {
        return description;

    }
}