package com.hundsun.jresplus.base.cache;

import java.util.List;

/**
 * @author sagahl<br>
 *         缓存管理类
 */
public interface CacheManager {

	/**
	 * 取得指定缓存的所有的缓存项
	 * 
	 * @param dictName
	 * @return
	 */
	public <E extends CacheEntry> List<E> getDatas(String dictName);

    /**
     * 取得指定缓存的所有的缓存项
     *
     * @param dictName
     * @return
     */
    @Deprecated
    public <E extends CacheEntry> List<E> getDates(String dictName);

	/**
	 * 取得指定缓存中的某一个缓存项
	 * 
	 * @param dictName
	 * @param key
	 * @return
	 */
	public <E extends CacheEntry> E getData(String dictName, String key);


    /**
     * 取得指定缓存中的某一个缓存项
     *
     * @param dictName
     * @param key
     * @return
     */
    @Deprecated
    public <E extends CacheEntry> E getDate(String dictName, String key);

	/**
	 * 重新构建缓存
	 * 
	 * @param dictName
	 * @param isLazy
	 *            是否延时加载
	 */
	public void reBuild(String dictName, boolean isLazy);
}
