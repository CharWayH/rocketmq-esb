package com.charwayh.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Description: 动态装载bean工具类
 * Author: SangYD
 * Date: 2018-09-17 16:37
 * Copyright (c) 2018, ewell.com
 * All Rights Reserved.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == SpringContextUtil.applicationContext) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }
}
