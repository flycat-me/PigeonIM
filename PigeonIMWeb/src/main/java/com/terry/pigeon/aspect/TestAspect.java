package com.terry.pigeon.aspect;

import com.terry.pigeon.annotation.TestAnnotation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-26 18:23
 **/
@Component
@Aspect
public class TestAspect {


    @Pointcut("@annotation(com.terry.pigeon.annotation.TestAnnotation)")
    private void testAspectAdvice(){
    }
    @Before("testAspectAdvice() && @annotation(testAnnotation)")
    public void before(TestAnnotation testAnnotation){
        System.out.println("--------切面方法被执行了-------");
        System.out.println("----------"+testAnnotation.limit()+"-------------");
    }
}
