package com.ljh.controller;

import com.ljh.domain.RequestResult;
import com.ljh.domain.User;
import com.ljh.domain.UserMongo;
import com.ljh.service.PayService;
import com.ljh.service.TestService;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/11 14:24
 */
@RestController
@RequestMapping("/api/test")
public class TestController {


	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TestService service;


	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	public String testInsert(){
		String result;
		try {
			service.insert();
			result = "success";
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}


	@RequestMapping(value = "/getUsers",method = RequestMethod.GET)
	public RequestResult getUsers(){
		List<User> user = service.getUser();
		Map<String,Object> map = new HashMap<>();
		map.put("users",user);
		String message = messageSource.getMessage("msg.instanceStatus.processed", null, LocaleContextHolder.getLocale());
		map.put("msg",message);
		RequestResult requestResult = new RequestResult(map);
		return requestResult;

	}


	@RequestMapping(value = "/redisTest",method = RequestMethod.POST)
	public String testRedis(@Param("code")String code){
		String result;
		try {
			service.redisTest(code);
			result = "success";
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@RequestMapping(value = "/getRedisCode",method = RequestMethod.GET)
	public String testRedis(){
		String result ;
		try {
			result = service.getRedis();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	@RequestMapping(value = "/mongoUser",method = RequestMethod.POST)
	public String testMongo(@RequestBody UserMongo userMongo){
		String result ;
		try {
			result = service.putUserMongo(userMongo);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}


	@RequestMapping(value = "/getUserMongo",method = RequestMethod.GET)
	public RequestResult getUserMongo(){
		return new RequestResult(service.getUserMongo());
	}


	@Autowired
	private PayService payService;

	@RequestMapping(value = "/createPayOrder",method = RequestMethod.POST)
	public Object createPayOrder(){
		Object pay = payService.createPay();
		return pay;
	}


	@RequestMapping(value = "/testRocketmq",method = RequestMethod.POST)
	public RequestResult testRocket(@Param("phone")String phone){

		String result = "success";
		try {
			service.sendMsg(phone);
		} catch (Exception e) {
			result = e.getMessage();
		}
		RequestResult requestResult = new RequestResult(result);
		return requestResult;
	}


	@RequestMapping(value = "/testLogin",method = RequestMethod.POST)
	public RequestResult testLogin(@Param("phone")String phone,@Param("code")String code){
		String result = "filed";
		try {
			boolean flag = service.testLogin(phone, code);
			if (flag){
				result = "success";
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		RequestResult requestResult = new RequestResult(result);
		return requestResult;
	}



}
