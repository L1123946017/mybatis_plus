package com.ljh.service;

import com.ljh.domain.User;
import com.ljh.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/10 15:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

	@Autowired
	private UserMapper userMapper;


	@Test
	public void test(){
		User user = new User();
		user.setAge(1);
		user.setUsername("张三");
		userMapper.insert(user);
	}




}
