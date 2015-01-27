package com.hundsun.jresplus.base.cache;

import java.util.Map;

/**
 * 
 * @author Leo 实现该接口可以让用户指定缓存内使用的map类型，默认是hashmap
 */
public interface CacheMap {

	/**
	 * 用于指定缓存所用的map类型，可以自己模拟
	 * 
	 * @return
	 */
	public <T> Map<String, T> getMap();

	/**
	 * 用户自己提供的map对象是否线程安全，如果不是线程安全是只读的不可以写
	 * 
	 * @return
	 */
	public boolean threadSafety();
}
