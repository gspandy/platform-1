package com.hundsun.jresplus.ui.contain;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.hundsun.jresplus.web.adapter.ReturnValueHandlerResolver;

/**
 * 
 * @author Leo
 * 
 */
@Component
public class PageletRequestParamResolver implements
		ReturnValueHandlerResolver {


	public static final String PAGELET_URL = "contain/pagelet";

	public Object resolveReturnValue(Method handlerMethod,
			Class<?> handlerType, Object returnValue,
			ExtendedModelMap implicitModel, NativeWebRequest webRequest) {
		if (webRequest.getAttribute(HornContainFilter.KEY,
				RequestAttributes.SCOPE_REQUEST) != null) {
			if (returnValue instanceof String
					&& ((String) returnValue).startsWith("redirect:")) {

				implicitModel.put("redirect",
						((String) returnValue).substring(9));

				return PAGELET_URL;
			}
		}

		return returnValue;
	}
}
