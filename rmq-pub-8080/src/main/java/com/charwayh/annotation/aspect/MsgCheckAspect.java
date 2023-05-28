package com.charwayh.annotation.aspect;

import com.charwayh.constant.Constant;
import com.charwayh.constant.MessageConstant;
import com.charwayh.entity.MQLog;
import com.charwayh.entity.MessageResult;
import com.charwayh.entity.Result;
import com.charwayh.esmapper.MQLogMapper;
import com.charwayh.manage.CacheManage;
import com.charwayh.util.DateUtil;
import com.charwayh.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author charwayH
 */
@Aspect
@Slf4j
@Component
public class MsgCheckAspect {

    @Pointcut("@annotation(com.charwayh.annotation.MsgCheck)")
    public void msgCheckAspect() {
    }

    @Around("msgCheckAspect()")
    public Object round(ProceedingJoinPoint proceedingJoinPoint) {
        MessageResult messageResult = null;
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            Map map = (Map) args[0];
            String producer = (String)map.get("producer");
            String consumer = (String)map.get("consumer");
            String topic = (String)map.get("topic");


            if(!CacheManage.qmSet.contains(producer)){
                return new Result(false, MessageConstant.PRODUCER_ABSENT.toString());
            }else if(!CacheManage.qmSet.contains(consumer)){
                return new Result(false, MessageConstant.CONSUMER_ABSENT.toString());
            }
            // TODO
//            else if(!CacheManage.topicSet.contains(topic)){
//                return new Result(false, MessageConstant.TOPIC_ABSENT.toString());
//            }


            messageResult = (MessageResult) proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return messageResult;
    }
}
