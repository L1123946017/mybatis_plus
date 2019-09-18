package com.ljh.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/9 11:16
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun-call")
@PropertySource(value = {"classpath:application.yml"})
public class SmsProperties {

	private String regionId;
	private String ak;
	private String sk;
	private String signName;
	private String templateCode;

}
