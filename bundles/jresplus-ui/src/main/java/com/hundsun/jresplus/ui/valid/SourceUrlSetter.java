package com.hundsun.jresplus.ui.valid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.hundsun.jresplus.web.contain.Contain;
import com.hundsun.jresplus.web.contain.async.AsynchronousContain;
import com.hundsun.jresplus.web.interceptors.AbstractHandlerInterceptor;

/**
 * 
 * @author Leo
 * 
 */
@Component
public class SourceUrlSetter extends AbstractHandlerInterceptor {
	Logger logger=LoggerFactory.getLogger(SourceUrlSetter.class);
	public static final String REQ_ATTR_KEY = "_screen_name";

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (AsynchronousContain.isAsyncConext()){
			return;
		}
		if (Contain.isInContain(request)){
			return;
		}
		if (modelAndView == null){
			return;
		}
		String viewName = modelAndView.getViewName();
		if(viewName.startsWith("forward:")){
			return;
		}
		if(viewName.startsWith("redirect:")){
			return;
		}
		request.setAttribute(REQ_ATTR_KEY, viewName);
		logger.debug("Set Request attribute[{}:{}]",REQ_ATTR_KEY,viewName);
	}

	public int getOrder() {
		return Integer.MIN_VALUE;
	}
}
