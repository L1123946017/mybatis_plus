package com.ljh.domain;

import lombok.Data;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/16 17:40
 */
@Data
public class RequestResult {
	private String msg;
	private String code;
	private Object obj;

	public RequestResult() {
	}

	public RequestResult(String msg) {
		this.msg = msg;
	}

	public RequestResult(Object obj) {
		this.obj = obj;
	}

	public RequestResult(String msg, String code) {
		this.msg = msg;
		this.code = code;
	}

	public RequestResult(String msg, Object obj) {
		this.msg = msg;
		this.obj = obj;
	}

	public RequestResult(String msg, String code, Object obj) {
		this.msg = msg;
		this.code = code;
		this.obj = obj;
	}
}
