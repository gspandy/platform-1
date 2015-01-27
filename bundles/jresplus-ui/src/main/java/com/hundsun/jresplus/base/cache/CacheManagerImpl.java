package com.hundsun.jresplus.base.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;

import com.hundsun.jresplus.beans.ObjectFactory;
import com.hundsun.jresplus.common.util.ArrayUtil;

/**
 * @author sagahl<br>
 */
public class CacheManagerImpl implements CacheManager, InitializingBean,
		ThreadSafetyCacheManager {
	private final static Logger log=LoggerFactory.getLogger(CacheManagerImpl.class);
	protected Map<String, CacheStore<CacheEntry>> datas = new HashMap<String, CacheStore<CacheEntry>>();

	@Autowired
	private ObjectFactory objectFactory;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void afterPropertiesSet() throws Exception {
		log.info("Default CacheManagerImpl inited.");
		List<GenericCacheBuilder> genericCacheBuilders = objectFactory
				.getBeansOfType4List(GenericCacheBuilder.class);
		if (genericCacheBuilders == null) {
			return;
		}
		if (ArrayUtil.isEmpty(genericCacheBuilders)) {
			log.info("no have cache implements.");
			return;
		}
		OrderComparator.sort(genericCacheBuilders);
		for (GenericCacheBuilder<?> builder : genericCacheBuilders) {
			try {
				datas.put(builder.getName(), new CacheStore(builder));
				log.info("regist CacheBuilder [{}]-[{}]",builder.getName(),builder.getClass());
			} catch (Exception e) {
				log.info("no have cache implements.", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends CacheEntry> List<E> getDatas(String dictName) {
		if (datas.containsKey(dictName)) {
			CacheStore<E> store = (CacheStore<E>) datas.get(dictName);
			Collection<E> list = store.getDatas();
			return list != null ? new ArrayList<E>(list) : null;
		}
		return null;
	}
    @Deprecated
    public <E extends CacheEntry> List<E> getDates(String dictName) {
        if (datas.containsKey(dictName)) {
            CacheStore<E> store = (CacheStore<E>) datas.get(dictName);
            Collection<E> list = store.getDatas();
            return list != null ? new ArrayList<E>(list) : null;
        }
        return null;
    }
	@SuppressWarnings("unchecked")
	public <E extends CacheEntry> E getData(String dictName, String key) {
		return (E) datas.get(dictName).getValue(key);
	}

    @Deprecated
    public <E extends CacheEntry> E getDate(String dictName, String key) {
        return (E) datas.get(dictName).getValue(key);
    }

	public void reBuild(String dictName, boolean isLazy) {
		if (datas.containsKey(dictName)) {
			datas.get(dictName).reBuild(isLazy);
		}
	}

	public void removeCacheEntries(String dictName, List<String> cacheEntries) {
		if (datas.containsKey(dictName) && datas.get(dictName).isThreadSafety()) {
			this.datas.get(dictName).remove(cacheEntries);
		}
	}

	public void addCacheEntries(String dictName, List<CacheEntry> cacheEntries) {
		if (datas.containsKey(dictName) && datas.get(dictName).isThreadSafety()) {
			this.datas.get(dictName).add(cacheEntries);
		}
	}

    public void addCacheEntry(String dictName, CacheEntry cacheEntry)
    {
        if (this.datas.containsKey(dictName)&& datas.get(dictName).isThreadSafety())
            ((CacheStore)this.datas.get(dictName)).add(cacheEntry);
    }

    public void updateCacheEntry(String dictName, CacheEntry cacheEntry)
    {
        if (this.datas.containsKey(dictName)&& datas.get(dictName).isThreadSafety())
            ((CacheStore)this.datas.get(dictName)).update(cacheEntry);
    }

    public void removeCacheEntry(String dictName, String key)
    {
        if (this.datas.containsKey(dictName)&& datas.get(dictName).isThreadSafety())
            ((CacheStore)this.datas.get(dictName)).remove(key);
    }
}
