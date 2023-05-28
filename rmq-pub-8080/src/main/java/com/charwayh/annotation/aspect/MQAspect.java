package com.charwayh.annotation.aspect;

import com.charwayh.entity.MQLog;
import com.charwayh.entity.MessageResult;
import com.charwayh.esmapper.MQLogMapper;
import com.charwayh.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    @Around("mqAspect()")
    public Object round(ProceedingJoinPoint proceedingJoinPoint){
        MessageResult messageResult = null;
        try {
            Object proceed = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            messageResult = (MessageResult)proceed;
            String messageId = messageResult.getMessageId();
            // 消息id不为空才存入es中
            if(messageId != null || !StringUtils.isEmpty(messageId)) {
                MQLog mqLog = new MQLog();
                Object[] args = proceedingJoinPoint.getArgs();
                mqLog.setProducer((String) args[0]);
                mqLog.setConsumer((String) args[1]);
                mqLog.setTopic((String) args[2]);
                mqLog.setMessageContent((String) args[3]);
                mqLog.setMsgId(messageResult.getMessageId());
                mqLog.setBusinessTime(DateUtil.getCurrentTime());
                mqLog.setBusinessType("暂未完成");
                mqLogMapper.save(mqLog);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return messageResult;
    }
}
