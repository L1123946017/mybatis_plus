package com.ljh.service;

import com.ljh.domain.RequestResult;
import com.ljh.domain.User;
import com.ljh.domain.UserMongo;

import java.util.List;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/11 13:43
 */
public interface TestService {
	void insert();

	List<User> getUser();

	void redisTest(String code);

	String getRedis();

	String putUserMongo(UserMongo userMongo);

	List<UserMongo> getUserMongo();

	void sendMsg(String phone) throws Exception;

	boolean testLogin(String phone, String code);
}
