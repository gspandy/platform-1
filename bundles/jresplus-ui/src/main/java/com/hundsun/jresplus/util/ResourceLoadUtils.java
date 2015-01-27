package com.hundsun.jresplus.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.hundsun.jresplus.beans.ObjectFactory;
import com.hundsun.jresplus.ui.web.HornFilter;

/**
 * 
 * @author Leo
 * 此代码已经废弃
 */
@Deprecated
@Component
public class ResourceLoadUtils implements ApplicationContextAware {

	private static final Map<String, Object> properties = new ConcurrentHashMap<String, Object>();

	public static final String HORN_URL = "/horn";

	public static <T> void setProperty(LabelValue<T> lv) {
		properties.put(lv.getLabel(), lv.getValue());
	}

	public static HttpSession getSession() {
		return HornFilter.getRequest().getSession();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(String key) {
		return (T) properties.get(key);
	}

	private static ObjectFactory OBJECT_FACTORY;

	public static ObjectFactory getObjectFactory() {
		return OBJECT_FACTORY;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		OBJECT_FACTORY = (ObjectFactory) applicationContext
				.getBean("objectFactory");
	}

}
