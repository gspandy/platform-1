package com.hundsun.jresplus.ui.tipmessage;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

/**
 * 
 * @author Leo
 * 
 */
@Component
public class TipMessagesArgumentResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter parameter,
			NativeWebRequest request) throws Exception {
		if (parameter.getParameterType().equals(TipMessages.class)) {
			if (request.getAttribute(TipMessages.TIP_NAME,
					RequestAttributes.SCOPE_SESSION) == null) {
				request.setAttribute(TipMessages.TIP_NAME, new TipMessages(),
						RequestAttributes.SCOPE_REQUEST);
			}
			return request.getAttribute(TipMessages.TIP_NAME,
					RequestAttributes.SCOPE_REQUEST);
		}
		return UNRESOLVED;
	}
}
