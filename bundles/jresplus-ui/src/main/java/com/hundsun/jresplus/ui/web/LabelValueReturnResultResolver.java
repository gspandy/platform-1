package com.hundsun.jresplus.ui.web;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

import com.hundsun.jresplus.util.LabelValue;
import com.hundsun.jresplus.web.adapter.ReturnValueHandlerResolver;
@Component
public class LabelValueReturnResultResolver implements
		ReturnValueHandlerResolver {

	public Object resolveReturnValue(Method handlerMethod,
			Class<?> handlerType, Object returnValue,
			ExtendedModelMap implicitModel, NativeWebRequest webRequest) {
		if (returnValue instanceof LabelValue) {
			LabelValue<?> labelValue = (LabelValue<?>) returnValue;
			implicitModel.put(labelValue.getLabel(), labelValue.getValue());
			return null;
		}
		return returnValue;
	}

}
