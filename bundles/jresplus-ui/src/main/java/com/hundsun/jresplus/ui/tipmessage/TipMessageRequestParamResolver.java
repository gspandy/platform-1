package com.hundsun.jresplus.ui.tipmessage;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.hundsun.jresplus.web.adapter.ReturnValueHandlerResolver;
@Component
public class TipMessageRequestParamResolver implements
		ReturnValueHandlerResolver {

	public Object resolveReturnValue(Method handlerMethod,
			Class<?> handlerType, Object returnValue,
			ExtendedModelMap implicitModel, NativeWebRequest webRequest) {

		String paramTipMessage = webRequest.getParameter(TipMessages.TIP_NAME);

		TipMessages tipMessages = (TipMessages) webRequest.getAttribute(
				TipMessages.TIP_NAME, RequestAttributes.SCOPE_REQUEST);

		if (paramTipMessage != null) {
			if (tipMessages == null) {
				tipMessages = new TipMessages();
			}
			tipMessages.add(paramTipMessage);
		}

		if (tipMessages != null) {
			if (tipMessages.size() > 0) {
				implicitModel.put(TipMessages.TIP_NAME, tipMessages);
			}
		}
		return returnValue;
	}

}
