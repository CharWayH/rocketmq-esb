package com.charwayh.annotation;

import java.lang.annotation.*;

/**
 * @author charwayH
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MQLog {
}
