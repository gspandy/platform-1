package com.hundsun.jresplus.ui.contain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.hundsun.jresplus.web.contain.async.AsynchronousContain;

/**
 * 
 * @author Leo
 * 
 */
@Component()
public class PageletExceptionResolver implements
		org.springframework.web.servlet.HandlerExceptionResolver, Ordered {
	Logger log=LoggerFactory.getLogger(PageletExceptionResolver.class);
	@Value("${system.dev.mode}")
	private boolean flag = false;

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		if (flag) {// 如果是开发模式，直接在页面上返回
			return null;
		} else {
			if (AsynchronousContain.isAsyncConext()) {
				log.error("pagelet request error",ex);
				request.setAttribute("tipMessage", ex.getMessage());
				return new ModelAndView(
						PageletRequestParamResolver.PAGELET_URL);
			}
			return null;
		}
	}

	public int getOrder() {
		return Integer.MAX_VALUE;
	}
}
