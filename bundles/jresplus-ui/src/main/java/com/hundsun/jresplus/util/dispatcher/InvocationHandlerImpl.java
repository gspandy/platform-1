package com.hundsun.jresplus.util.dispatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * @author Leo
 * 
 * @param <T>
 */
public class InvocationHandlerImpl<T> implements InvocationHandler {

	List<T> list = null;

	public InvocationHandlerImpl(List<T> list) {
		this.list = list;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.isAnnotationPresent(Dispatcher.class) == false) {// 需要有注解
			throw new DispatcherRuntimeException("no have annotation!");
		}
		if (list == null || list.size() == 0) {
			return null;
		}
		try {
			if (list.size() == 1) {
				return method.invoke(list.get(0), args);
			}
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
		if (void.class != method.getReturnType()) {// 需要没有返回值
			throw new DispatcherRuntimeException(" return type must be void! ");
		}
		for (T t : list) {
			try {
				method.invoke(t, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
		return null;
	}

}
