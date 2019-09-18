package com.ljh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.ljh.properties.PayProperties;
import com.ljh.service.PayService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/16 14:26
 */
@Service
public class PayServiceImpl implements PayService {

	@Autowired
	private PayProperties payProperties;


	@Override
	public Object createPay(){
		AlipayTradeCreateResponse response = null;
		AlipayClient alipayClient = new DefaultAlipayClient(payProperties.getUrl()
				,payProperties.getAppId(),payProperties.getAppPricateKey());
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		Map<String,String> param = new HashMap<>();
		param.put("out_trade_no",UUID.randomUUID().toString());
		param.put("total_amount","0.01");
		param.put("subject","牙刷");
		String params = JSONObject.toJSONString(param);
		request.setBizContent(params);
		try {
			response = alipayClient.execute(request);
			System.out.println(JSONObject.toJSONString(response));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return response;
	}


	@Override
	public void pay(){
		AlipayClient alipayClient = new DefaultAlipayClient(payProperties.getUrl()
				,payProperties.getAppId(),payProperties.getAppPricateKey());
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		Map<String,Object> map = new HashMap<>();
		map.put("out_trade_no", UUID.randomUUID().toString());
		map.put("product_code", "FAST_INSTANT_TRADE_PAY");
		map.put("total_amount", 0.01);
		map.put("subject", "牙刷");
		String params = JSONObject.toJSONString(map);
		request.setBizContent(params);
		try {
			AlipayTradePagePayResponse response = alipayClient.execute(request);
			System.out.println(JSONObject.toJSONString(response));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

	}

}

