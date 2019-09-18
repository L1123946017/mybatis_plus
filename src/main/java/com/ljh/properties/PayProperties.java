package com.ljh.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/16 14:35
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
@PropertySource(value = {"classpath:application.yml"})
public class PayProperties {

	private String url;
	private String appId;
	private String appPricateKey;
	private String alipayPublicKey;
}
