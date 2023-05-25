package com.charwayh.annotation.aspect;

import com.charwayh.entity.MQLog;
import com.charwayh.entity.MessageResult;
import com.charwayh.mapper.MQLogMapper;
import com.charwayh.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author charwayH
 */
@Aspect
@Slf4j
@Component
public class MQAspect {

    @Autowired
    private MQLogMapper mqLogMapper;


    @Pointcut("@annotation(com.charwayh.annotation.MQLog)")
    public void mqAspect() {
    }

//    @Before("mqAspect()")
//    public void beforeCwLog(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date date = new Date();
//        String time = simpleDateFormat.format(date);
//
//
//    }
//
//    @After("mqAspect()")
//    public void afterCwLog(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date date = new Date();
//        String time = simpleDateFormat.format(date);
//        log.info(time + "调用→" + methodName + "====end======");
//        Object[] args = joinPoint.getArgs();
//        String msg = (String)args[2];
//        MQLog mqLog = new MQLog();
//        mqLog.setMsg(msg);
//        mqLog.setTime(time);
//        mqLogMapper.save(mqLog);
//    }

    @Around("mqAspect()")
    public Object round(ProceedingJoinPoint proceedingJoinPoint){
        MessageResult messageResult = null;
        try {
            Object proceed = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            messageResult = (MessageResult)proceed;
            MQLog mqLog = new MQLog();
            mqLog.setProducer(messageResult.getProducer());
            mqLog.setConsumer(messageResult.getConsumer());
            mqLog.setTopic(messageResult.getTopic());
            mqLog.setMessageContent(messageResult.getMessageContent());
            mqLog.setMsgId(messageResult.getMessageId());
            mqLog.setBusinessTime(DateUtil.getCurrentTime());
            mqLog.setBusinessType("暂未完成");
            mqLogMapper.save(mqLog);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return messageResult;
    }


}
