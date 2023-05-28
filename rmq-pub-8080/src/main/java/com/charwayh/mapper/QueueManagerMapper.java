package com.charwayh.mapper;

import com.charwayh.upon.domain.QueueManager;
import com.charwayh.vo.QueueManagerVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author: create by CharwayH
 * @description: 队列管理器配置持久层
 * @date:2023/5/28
 */
public interface QueueManagerMapper extends Mapper<QueueManager> {

    //---------新增
    Integer insertQmgr(QueueManager queueManager);

    Integer batchInsertQmgr(@Param("qmgrList") List<QueueManager> queueManagers);

    //--------刪除
    Integer deleteQmgrById(Integer id);

    Integer deleteQmgrByBscId(@Param("bscId") Integer bscId);

    //--------修改
    Integer updateQmgr(QueueManager queueManager);

    Integer updateQmgrStatus(QueueManager queueManager);

    Integer switchLoad(QueueManager queueManager);

    //--------查詢
    @Select("select * from sdk_queue_manager ORDER BY qm_name")
    List<QueueManager> getQueueManagerList();

    List<QueueManager> getQueueManagerListByBscId(Integer bscId);

    List<QueueManager> getQueueManagerListByQmIds(@Param("qmIds") List<Integer> qmIds);

    QueueManager getQueueManagerByNameAndBscId(@Param(("queueManagerName")) String queueManagerName, @Param("sysId") Integer sysId);

    Integer getQmidByQmName(String qmName);

    @Select("select * from sdk_queue_manager where qm_ip =#{ip} and qm_name =#{qmName} and qm_pid=#{qmPid}  ORDER BY qm_name")
    List<QueueManager> getFrontQmsByQmNameAndIp(@Param("qmName") String qmName, @Param("ip") String ip, @Param("qmPid") Integer qmPid);

    List<QueueManager> getQueueManagerListByPid(Integer pid);

    List<QueueManager> getQueueManagersByPname(String pName);

    QueueManager getQueueManagerByQmName(String queueManagerName);

    QueueManager getQmgrByQmid(@Param(("qmId")) Integer qmId);

    @Select("select * from sdk_queue_manager where bsc_id = #{bscId} and qm_pid = 0 ")
    List<QueueManager> getLoadQms(@Param(("bscId")) Integer bscId);

    List<QueueManager> getFrontQms(@Param(("bscId")) Integer bscId, @Param(("qmPid")) Integer qmPid);

    //--------查詢
    @Select("select * from sdk_queue_manager where qm_is_master='1' and qm_pid !='0' ")
    List<QueueManager> getMasterQMList();

    List<QueueManager> getMasterQMListBySystem(@Param(("sysCode")) String sysCode);

    List<QueueManagerVo> queryMqInfoByIp(@Param("ip")String ip);

    List<QueueManagerVo> getReqrespMQInfo(@Param(("sncReqrespIp"))String sncReqrespIp);

    List<QueueManagerVo> getPubsubMQInfo(@Param(("sncPubsubIp"))String sncPubsubIp);
    List<QueueManagerVo> getLoadMqInfo();
}
