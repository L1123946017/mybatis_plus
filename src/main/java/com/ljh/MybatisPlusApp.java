package com.ljh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/10 11:26
 */
@SpringBootApplication
@MapperScan("com.ljh.mapper")
public class MybatisPlusApp {
	public static void main(String[] args) {
		SpringApplication.run(MybatisPlusApp.class,args);
	}
}
