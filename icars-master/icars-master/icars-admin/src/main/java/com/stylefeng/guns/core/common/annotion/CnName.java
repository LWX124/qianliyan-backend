package com.stylefeng.guns.core.common.annotion;

import java.lang.annotation.*;

/**
 * 常量中文名
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CnName {
    String value() default "";
}