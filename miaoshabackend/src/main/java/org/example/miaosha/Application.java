package org.example.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.miaosha"})
@MapperScan("org.example.miaosha.dao")
public class Application {

    public static void main( String[] args ) {
        SpringApplication.run(Application.class,args);
    }

}
