package com.charwayh.mapper;

import com.charwayh.upon.domain.Queue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.mapper
 * @date:2023/5/28
 */
public interface QueueMapper extends Mapper<Queue> {
    //---------新增
    Integer insertQueue(Queue queue);

    List<Queue> batchInsertQueue(@Param("queueList") List<Queue> queueList);

    //--------刪除
    Integer deleteQueue(Integer qId);

    Integer deleteQueueByQmId(@Param("qmId") Integer qmId);

    Integer batchDeleteQueueByQmIds(@Param("qmIds") List<Integer> qmIds);

    //--------修改
    Integer updateQueue(Queue queue);

    Integer updateQueueStatus(Queue queue);

    //--------查詢
    @Select("select * from sdk_queue where q_id =#{qId}  ")
    Queue getByQid(@Param("qId") Integer qId);

    @Select("select * from sdk_queue ORDER BY q_get_status desc ,q_name_get  ")
    List<Queue> getQueueList();

    List<Queue> getQueueListByQmIdList(@Param("qmIds") List<Integer> qmIdList);

    List<Queue> getQueueListByCondition(@Param("qmId") Integer qmId, @Param("channel") String channel);

    Queue getQueueByQmIdAndChannel(@Param("qmId") Integer qmId, @Param("channel") String channel);

    //--------统计
    @Select("select count(*) from sdk_queue where qm_id = #{qmId} and (q_name_put like concat('%',#{channel},'%') or q_name_get like concat('%',#{channel},'%') or q_name_collect  like concat('%',#{channel},'%')) and q_id !=#{qId} ")
    Integer countFrontQExcludeThisQId(@Param(("qmId")) Integer qmId, @Param(("channel")) String channel, @Param(("qId")) Integer qId);

}
