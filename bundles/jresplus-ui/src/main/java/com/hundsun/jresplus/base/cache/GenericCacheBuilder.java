package com.hundsun.jresplus.base.cache;

import java.util.Collection;

/**
 * 标准的缓存实现接口，在实现该接口同时实现cacheMap的接口，用于指定缓存的数据结构，实现该接口，系统启动的时候即会加载缓存
 * 
 * @author sagahl<br>
 */
public interface GenericCacheBuilder<T extends CacheEntry> {

	/**
	 * 系统启动的时候会调用该接口用于读取缓存的数据
	 * 
	 * @return
	 */
	public Collection<T> build();

	/**
	 * 指定缓存的名称，一个系统中的缓存名称不可以相同
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 该缓存是否可以延时加载，如果是延时加载，则在调用的时候才加载
	 * 
	 * @return
	 */
	public boolean canLazyBulid();
}
