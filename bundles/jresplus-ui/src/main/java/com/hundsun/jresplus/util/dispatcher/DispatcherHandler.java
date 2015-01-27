package com.hundsun.jresplus.util.dispatcher;

/**
 * 
 * @author Leo
 * 
 */
public interface DispatcherHandler {

	public <T> T getDispatcher(Class<T> clazz);
}
