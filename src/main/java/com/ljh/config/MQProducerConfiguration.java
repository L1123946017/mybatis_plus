package com.ljh.config;

import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/17 13:33
 */
@Configuration
public class MQProducerConfiguration {
	public static final Logger LOGGER = LoggerFactory.getLogger(MQProducerConfiguration.class);


	@Value("${rocketmq.producer.groupName}")
	private String groupName;

	@Value("${rocketmq.producer.namesrvAddr}")
	private String namesrvAddr;

	@Value("${rocketmq.producer.maxMessageSize}")
	private Integer maxMessageSize;

	@Value("${rocketmq.producer.sendMsgTimeout}")
	private Integer sendMsgTimeout;

	@Value("${rocketmq.producer.retryTimesWhenSendFailed}")
	private Integer retryTimesWhenSendFailed;

	@Bean
	public DefaultMQProducer getRocketMQProduct() throws Exception {
		if (StringUtils.isEmpty(this.groupName)) {
			throw new Exception("groupName is blank");
		}

		if (StringUtils.isEmpty(this.namesrvAddr)) {
			throw new Exception("nameServerAddr is blank");
		}

		DefaultMQProducer producer;
		//设置服务名
		producer = new DefaultMQProducer(this.groupName);
		//设置地址
		producer.setNamesrvAddr(this.namesrvAddr);
		//设置主题
		//producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");

		if(this.maxMessageSize!=null){
			producer.setMaxMessageSize(this.maxMessageSize);
		}
		if(this.sendMsgTimeout!=null){
			producer.setSendMsgTimeout(this.sendMsgTimeout);
		}

		if(this.retryTimesWhenSendFailed!=null){
			producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
		}

		try {
			producer.start();
		} catch (MQClientException e) {
			throw new Exception("start is filed");
		}

		return producer;
	}


}
