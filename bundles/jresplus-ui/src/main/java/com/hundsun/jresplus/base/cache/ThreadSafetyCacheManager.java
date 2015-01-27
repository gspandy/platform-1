package com.hundsun.jresplus.base.cache;

import java.util.List;

/**
 * 缓存线程安全管理接口，配合cachemap使用
 * 
 * @author Leo
 * 
 */
public interface ThreadSafetyCacheManager {

	/**
	 * 可以对线程安全的字典做字典项的添加
	 * 
	 * @param dictName
	 * @param cacheEntries
	 */
	public void addCacheEntries(String dictName, List<CacheEntry> cacheEntries);

	/**
	 * 可以对线程安全的字典项的删除
	 * 
	 * @param dictName
	 * @param cacheEntries
	 */
	public void removeCacheEntries(String dictName, List<String> cacheEntries);

    public abstract void addCacheEntry(String dictName, CacheEntry cacheEntry);

    public abstract void updateCacheEntry(String dictName, CacheEntry cacheEntry);

    public abstract void removeCacheEntry(String dictName, String key);

}
