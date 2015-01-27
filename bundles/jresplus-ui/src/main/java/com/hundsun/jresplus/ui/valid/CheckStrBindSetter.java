package com.hundsun.jresplus.ui.valid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.hundsun.jresplus.common.util.StringUtil;
import com.hundsun.jresplus.web.adapter.AnnotationMethodHandlerInterceptorAdapter;

/**
 * 
 * @author Leo
 * 
 */
@Component
public class CheckStrBindSetter extends
		AnnotationMethodHandlerInterceptorAdapter {
	private final static Logger log=LoggerFactory.getLogger(CheckStrBindSetter.class);
	@Autowired
	private CheckStrBind checkStrBind;

	public void preInvoke(Method handlerMethod, Object handler,
			ServletWebRequest webRequest) {
		Annotation[][] annotations = handlerMethod.getParameterAnnotations();
		for (int n = 0; n < annotations.length; n++) {
			for (Annotation annotation : annotations[n]) {
				if (annotation.annotationType() == Bind.class) {
					if(log.isDebugEnabled()){
						log.debug("request[{}] bind checkStr",webRequest.getRequest().getServletPath());
					}
					Bind bind = (Bind) annotation;
					String groupStr = array2String(bind.groups());
					String value = null;
					for (Annotation annotationTemp : annotations[n]) {
						if (annotationTemp.annotationType() == RequestParam.class) {
							RequestParam requestParam = (RequestParam) annotationTemp;
							if (StringUtil.isEmpty(requestParam.value()) == false) {
								value = requestParam.value();
							}
							break;
						}
					}
					Map<String, String> map = new HashMap<String, String>();
					if (value != null) {
						checkStrBind.getCheckStr(value, annotations[n], map);
					}
					checkStrBind.getCheckStr(null,
							handlerMethod.getParameterTypes()[n], map);
					for (Entry<String, String> entry : map.entrySet()) {
						String checkStrKey = entry.getKey() + ".checkStr";
						String groupStrKey = entry.getKey() + ".checkGroup";
						String checkStr = entry.getValue();
						webRequest.setAttribute(checkStrKey,
								checkStr,
								RequestAttributes.SCOPE_REQUEST);
						webRequest.setAttribute(groupStrKey,
								groupStr, RequestAttributes.SCOPE_REQUEST);
						if(log.isDebugEnabled()){
							log.debug("request attribute[{}]:[{}]",checkStrKey,checkStr);
							log.debug("request attribute[{}]:[{}]",groupStrKey,groupStr);
						}
					}
				}
			}
		}
	}

	public String array2String(String[] args) {
		StringBuffer sb = new StringBuffer();
		for (String str : args) {
			sb.append(str).append(";");
		}
		if (sb.length() > 2) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
