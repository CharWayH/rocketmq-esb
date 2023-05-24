package com.charwayh.entity;

import java.io.Serializable;

/**
 * @author charwayH
 */
public class MessageResult implements Serializable {
    private String messageId;

    private String result;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
