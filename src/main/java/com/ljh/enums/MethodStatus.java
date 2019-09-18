package com.ljh.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/17 15:59
 */
public enum MethodStatus implements IEnum<String> {
	ADD("ADD"),
	UPDATE("UPDATE"),
	DELETE("DELETE"),
	INSERT("INSERT"),
	;
	private String code;


	MethodStatus(String code) {
		this.code = code;

	}

	public static MethodStatus getByCode(String code) {
		List<MethodStatus> methodStatus = Arrays.asList(MethodStatus.values());
		Optional<MethodStatus> methodStatusType = methodStatus.stream()
				.filter(it -> it.getCode().equals(code))
				.findFirst();
		return methodStatusType.orElse(null);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String getValue() {
		return code;
	}
}
