package com.charwayh.mapper;

import com.charwayh.entity.MQLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author charwayH
 */
@Repository
public interface MQLogMapper extends ElasticsearchRepository<MQLog, Long> {

}
