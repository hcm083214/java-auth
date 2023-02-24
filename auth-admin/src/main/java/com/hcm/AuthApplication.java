package com.hcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author pc
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);
    }
}
