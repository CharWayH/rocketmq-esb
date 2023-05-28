package com.charwayh.mapper;

import com.charwayh.upon.domain.BusinessSystemConf;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author: create by CharwayH
 * @description: 业务系统配置持久层
 * @date:2023/5/28
 */
public interface BusinessSystemConfMapper extends Mapper<BusinessSystemConf> {

    //------新增
    Integer addBusinessConf(BusinessSystemConf systemConf);

    //------刪除
    Integer deleteBusinessConf(Integer bscId);

    //------修改
    Integer editBusinessConf(BusinessSystemConf systemConf);

    Integer updateClientIp(@Param("bscId") Integer bscId, @Param("clientIp") String clientIp);

    //------查詢
    @Select("select * from sdk_business_system_conf order by bsc_create_time desc")
    List<BusinessSystemConf> getConfList();

    @Select("select * from sdk_business_system_conf order by bsc_code")
    List<BusinessSystemConf> getConfListOrderByCode();

    BusinessSystemConf getConfByCode(@Param("bscCode") String bscCode);

    BusinessSystemConf getConfByCondition(@Param("sysCode") String sysCode, @Param("key") String key);

    List<BusinessSystemConf> getConfByBscIds(@Param("bscIds") List<Integer> bscIds, @Param("page") Page page);

    String getKeyBySysCode(String sysCode);

    List<BusinessSystemConf> getAllConf();

    BusinessSystemConf getConfById(Integer bscId);

    List<Integer> complexQueryByBscIdAndChannel(@Param("bscId") Integer bscId, @Param("channel") String channel);

    @Select("select bsc_id,bsc_code,bsc_name from sdk_business_system_conf order by bsc_code")
    List<BusinessSystemConf> getBusinessSystemList();

    //------统计
    Integer getCountByName(String bscName);

    Integer getCountByCode(String bscCode);

    Integer getCountByCodeAndKeyAndIp(String sysCode, String key, String ip);

}
