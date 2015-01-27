package com.hundsun.jresplus.util.dispatcher;

import java.lang.reflect.Proxy;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hundsun.jresplus.beans.ObjectFactory;

/**
 * 
 * @author Leo
 * 
 */
@Service("dispatcherHandler")
public class DispatcherHandlerImpl implements DispatcherHandler {

	@Autowired
	private ObjectFactory objectFactory;

	@SuppressWarnings("unchecked")
	public <T> T getDispatcher(Class<T> clazz) {
		List<T> list = objectFactory.getBeansOfType4List(clazz);
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[] { clazz }, new InvocationHandlerImpl<T>(list));
	}
}