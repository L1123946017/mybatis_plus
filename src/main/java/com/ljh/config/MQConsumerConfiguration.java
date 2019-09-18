package com.ljh.config;

import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/17 13:41
 */
@Configuration
public class MQConsumerConfiguration {
	public static final Logger logger = LoggerFactory.getLogger(MQConsumerConfiguration.class);

	@Value("${rocketmq.consumer.groupName}")
	private String groupName;

	@Value("${rocketmq.consumer.namesrvAddr}")
	private String namesrvAddr;

	@Value("${rocketmq.consumer.consumeThreadMin}")
	private int consumeThreadMin;
	@Value("${rocketmq.consumer.consumeThreadMax}")
	private int consumeThreadMax;
	@Value("${rocketmq.consumer.topics}")
	private String topics;
	@Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
	private int consumeMessageBatchMaxSize;

	@Autowired
	private MQMessageListener listener;


	@Bean
	public DefaultMQPushConsumer getRocketMQConsumer() throws Exception {
		if (StringUtils.isEmpty(groupName)) {
			throw new Exception("groupName is null !!!");
		}
		if (StringUtils.isEmpty(namesrvAddr)) {
			throw new Exception("namesrvAddr is null !!!");
		}
		if (StringUtils.isEmpty(topics)) {
			throw new Exception("topics is null !!!");
		}

		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.groupName);
		consumer.setNamesrvAddr(this.namesrvAddr);
		consumer.setConsumeThreadMin(consumeThreadMin);

		//consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		//consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
		consumer.subscribe(topics, "*");
		//consumer.setMessageModel(MessageModel.CLUSTERING);

		consumer.registerMessageListener(listener);

		consumer.start();

		return consumer;

	}

}
