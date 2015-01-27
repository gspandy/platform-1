package com.hundsun.jresplus.ui.web;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class AllParamsArgumentResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter parameter,
			NativeWebRequest request) throws Exception {
		if (parameter.getParameterType().equals(Map.class)) {
			return request.getParameterMap();
		}
		return UNRESOLVED;
	}
}
