package com.hundsun.jresplus.ui.valid;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import com.hundsun.jresplus.ui.contain.PageletRequestParamResolver;
import com.hundsun.jresplus.web.contain.async.AsynchronousContain;

/**
 * 
 * @author Leo
 * 
 */
@Component()
public class BindExceptionResolver implements
		org.springframework.web.servlet.HandlerExceptionResolver, Ordered {
	private final static Logger log = LoggerFactory
			.getLogger(BindExceptionResolver.class);
	@Value("${horn.ui.vaild.return.param.name}")
	private String validReturnParameterName = "sourceurl";

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		if (!(ex instanceof BindException)) {
			return null;
		}
		log.debug("Bind exception,url:[{}]", request.getServletPath());
		BindException bindException = (BindException) ex;
		setValidBackField(request, bindException);
		if (AsynchronousContain.isAsyncConext()) {
			return new ModelAndView(PageletRequestParamResolver.PAGELET_URL);
		}
		String validSourceUrl = request.getParameter(validReturnParameterName);
		if (StringUtils.isBlank(validSourceUrl)) {
			return null;
		}
		ModelAndView modelAndView = new ModelAndView(validSourceUrl);
		if (request.getAttribute(SourceUrlSetter.REQ_ATTR_KEY) == null) {
			request.setAttribute(SourceUrlSetter.REQ_ATTR_KEY, validSourceUrl);
			log.debug("Set Request attribute[{}:{}]",SourceUrlSetter.REQ_ATTR_KEY,validSourceUrl);
		}
		setValidFieldValue(request, modelAndView);
		return modelAndView;
	}

	private void setValidFieldValue(HttpServletRequest request,
			ModelAndView modelAndView) {
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paramName = enums.nextElement();
			String paramValue = request.getParameter(paramName);
			modelAndView.addObject(paramName, paramValue);
			log.debug("valid source param[{}]:[{}]", paramName, paramValue);
		}
	}

	private void setValidBackField(HttpServletRequest request,
			BindException bindException) {
		ToBackField backField = (ToBackField) request
				.getAttribute("ToBackField");
		if (backField == null) {
			backField = new ToBackField();
		}
		for (FieldError fieldError : bindException.getBindingResult()
				.getFieldErrors()) {
			backField.addBackField(fieldError.getField(),
					fieldError.getDefaultMessage());
			log.debug(fieldError.toString());
		}
		request.setAttribute("ToBackField", backField);
	}

	public int getOrder() {
		return Integer.MAX_VALUE;
	}
}
