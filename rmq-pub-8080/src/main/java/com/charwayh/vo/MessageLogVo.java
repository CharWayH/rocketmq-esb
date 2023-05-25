package com.charwayh.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.vo
 * @date:2023/5/25
 */
@Data
public class MessageLogVo implements Serializable {
    private String messageId;
    private String result;
    private String messageContent;
    private String time;
    /**
     * 业务实际 P B
     */
    private String businessTime;
}
