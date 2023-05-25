package com.charwayh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author charwayH
 */
@Data
public class MessageResult implements Serializable {
    private String messageId;

    private String result;
}
