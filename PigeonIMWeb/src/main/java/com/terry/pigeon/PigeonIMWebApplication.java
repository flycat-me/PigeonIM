package com.terry.pigeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author terry_lang
 * @description
 * @since 2022-02-28 10:48
 **/
@SpringBootApplication
//SpringBoot启动器，排除数据库依赖驱动
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableWebSocket
@EnableAspectJAutoProxy
public class PigeonIMWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PigeonIMWebApplication.class,args);
    }
}
