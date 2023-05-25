package com.charwayh.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @author charwayH
 */
@Document(indexName = "mq-log")
@Data
public class MQLog {
    private String id;
    private String msg;
    private String msgId;
    private String time;
}
