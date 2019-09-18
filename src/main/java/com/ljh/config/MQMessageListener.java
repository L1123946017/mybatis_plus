package com.ljh.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.JsonObject;
import com.ljh.enums.MethodStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/17 15:49
 */
@Component
public class MQMessageListener implements MessageListenerConcurrently {
	public static final Logger logger = LoggerFactory.getLogger(MQProducerConfiguration.class);


	private static final String DOMAIN_ID = "dysmsapi.aliyuncs.com";
	private static final String VERSION_ID = "2017-05-25";
	private static final String ACTION_ID = "SendSms";

	@Autowired
	private SmsProperties properties;

	@Autowired
	private RedisTemplate redisTemplate;


	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
		if (CollectionUtils.isEmpty(list)) {
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		try {
			MessageExt messageExt = list.get(0);
			String phone = new String(messageExt.getBody());
			sendSms(phone);
			System.out.println(phone + ">>>>>>>>>>>>>>");
		} catch (Exception e) {
			logger.info(e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}


	private void sendSms(String phone) throws Exception {
		try {
			DefaultProfile profile = DefaultProfile.getProfile(properties.getRegionId(), properties.getAk(), properties.getSk());
			IAcsClient client = new DefaultAcsClient(profile);
			CommonRequest request = new CommonRequest();
			request.setSysMethod(MethodType.POST);
			request.setDomain(DOMAIN_ID);
			request.setVersion(VERSION_ID);
			request.setAction(ACTION_ID);
			request.putQueryParameter("PhoneNumbers", phone);
			request.putQueryParameter("SignName", properties.getSignName());
			request.putQueryParameter("TemplateCode", properties.getTemplateCode());
			Map<String, String> map = new HashMap<>();

			String code = randomCode();
			map.put("code", code);
			redisTemplate.opsForValue().set("sms" + phone, code,120, TimeUnit.SECONDS);
			String string = JSON.toJSONString(map);
			request.putQueryParameter("TemplateParam", string);
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ClientException e) {
			throw new Exception("snedSms is Filed");
		}
	}


	public static String randomCode() {
		StringBuilder str = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			str.append(random.nextInt(10));
		}
		return str.toString();
	}

	public static void main(String[] args) {
		System.out.println(randomCode());
	}

}
