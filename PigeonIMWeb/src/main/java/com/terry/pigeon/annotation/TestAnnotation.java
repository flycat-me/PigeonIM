package com.terry.pigeon.annotation;

import java.lang.annotation.*;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-26 18:22
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestAnnotation {
    //标识 指定sec时间段内的访问次数限制
    int limit() default 5;
    //标识 时间段
    int sec() default 5;
}
