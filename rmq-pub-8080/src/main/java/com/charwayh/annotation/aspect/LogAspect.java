package com.charwayh.annotation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author charwayH
 */
@Aspect
@Slf4j
@Component
public class LogAspect {
    @Pointcut("@annotation(com.charwayh.annotation.CwLog)")
    public void cwLogAspect(){}

    @Before("cwLogAspect()")
   public void beforeCwLog(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        log.info(time + "调用→" + methodName + "====begin======" );
   }

   @After("cwLogAspect()")
   public void afterCwLog(JoinPoint joinPoint){
       String methodName = joinPoint.getSignature().getName();
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
       Date date = new Date();
       String time = simpleDateFormat.format(date);
       log.info(time + "调用→" + methodName + "====end======" );
   }

}
