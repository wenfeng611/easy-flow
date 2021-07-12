package com.auditflow.customize;

import com.auditflow.customize.utils.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@MapperScan("com.auditflow.customize.mapper")
@SpringBootApplication
public class CustomizeApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CustomizeApplication.class, args);
        SpringContextUtils.setContext(applicationContext);
    }

}
