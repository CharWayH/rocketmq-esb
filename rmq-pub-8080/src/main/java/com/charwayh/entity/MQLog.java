package com.charwayh.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @author charwayH
 */
@Document(indexName = "mq-log")
@Data
public class MQLog {
    @Id
    private String msgId;
    private String producer;
    private String consumer;
    private String topic;
    private String messageContent;
    /**
     * 业务实际 P B
     */
    private String businessType;

    private String businessTime;
}
