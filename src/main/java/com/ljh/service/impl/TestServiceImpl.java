package com.ljh.service.impl;

import com.ljh.config.MQProducerConfiguration;
import com.ljh.domain.User;
import com.ljh.domain.UserMongo;
import com.ljh.mapper.UserMapper;
import com.ljh.service.TestService;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/11 13:21
 */
@Service
public class TestServiceImpl implements TestService {

	public static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private DefaultMQProducer mqProducer;


	@Override
	public void insert() {
		User user = new User();
		user.setAge(1);
		user.setUsername("张三");
		userMapper.insert(user);
	}

	@Override
	public List<User> getUser() {
		List<User> users = userMapper.selectAll();
		return users;
	}


	@Override
	public void redisTest(String code) {
		redisTemplate.opsForValue().set("test_one", code, 60, TimeUnit.SECONDS);
	}

	@Override
	public String getRedis() {
		String result = (String) redisTemplate.opsForValue().get("test_one");
		return result;
	}

	@Override
	public String putUserMongo(UserMongo userMongo) {
		try {
			mongoTemplate.save(userMongo);
		} catch (Exception e) {
			throw e;
		}
		return "success";
	}

	@Override
	public List<UserMongo> getUserMongo() {
		Query query = new Query();
		//query.addCriteria(Criteria.where("username").is("Jiahui Li"));
		Criteria.where("username").is("Jiahui Li")
				.and("").is("");
		//query.addCriteria(Criteria.where("").is(""));
		//Query queryList = Query.query(Criteria.where("").is(""));
		List<UserMongo> mongos = mongoTemplate.findAll(UserMongo.class);
		return mongos;
	}

	@Override
	public void sendMsg(String phone) throws Exception{
		logger.info("开始发送消息：" + phone);
		Message sendMsg = new Message("DemoTopic", "DemoTag", phone.getBytes());

		SendResult sendResult = mqProducer.send(sendMsg);
		logger.info("消息发送响应信息：" + sendResult.toString());

	}



	@Override
	public boolean testLogin(String phone, String code) {
		boolean flag = false;
		String codeChk = (String) redisTemplate.opsForValue().get("sms" + phone);
		if (!StringUtils.isEmpty(codeChk) && code.equals(codeChk)) {
			flag = true;
		}
		return flag;
	}


}
