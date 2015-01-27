/*
 * 修改记录
 * 2014-6-24	XIE		TASK #10553 cacheManager目前对于延迟特性的存储单元直接调用getValue时没法触发数据加载操作
 */
package com.hundsun.jresplus.base.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.jresplus.common.util.ArrayUtil;

/**
 * @author sagahl<br>
 */
public class CacheStore<T extends CacheEntry> {
	private final static Logger log=LoggerFactory.getLogger(CacheStore.class);
	private AtomicBoolean freshing = new AtomicBoolean(false);

	private Map<String, T> datas;
	private GenericCacheBuilder<T> genericCacheBuilder;

	private boolean threadSafety = false;

	public boolean isThreadSafety() {
		return threadSafety;
	}

	public CacheStore(GenericCacheBuilder<T> genericCacheBuilder) {
		this.genericCacheBuilder = genericCacheBuilder;
		if (genericCacheBuilder.canLazyBulid() == false) {
			if (refresh() == false) {
				log.info("cache refresh error!");
			}
		}
	}

	public void add(List<T> datas) {
		if (this.datas != null) {
			for (T t : datas) {
				this.datas.put(t.getKey(), t);
			}
		}
	}

	public void remove(List<String> datas) {
		if (this.datas != null) {
			for (String str : datas) {
				this.datas.remove(str);
			}
		}
	}
    public void add(T entry){
        if (this.datas != null){
            this.datas.put(entry.getKey(), entry);
        }
    }

    public void update(T entry){
        if (this.datas != null){
            this.datas.put(entry.getKey(), entry);
        }

    }

    public void remove(String key){
        if (this.datas != null){
            this.datas.remove(key);
        }
    }
	public T getValue(String label) {
		if (datas == null) {
			if (refresh() == false) {
				return null;
			}
		}
		return datas == null ? null : datas.get(label);
	}

	public Collection<T> getDatas() {
		if (datas == null) {
			if (refresh() == false) {
				return null;
			}
		}
		return datas.values();
	}

	public boolean reBuild(boolean isLazy) {
		if (genericCacheBuilder.canLazyBulid() == true && isLazy == true) {
			datas = null;
			return true;
		} else {
			return refresh();
		}
	}

	private boolean refresh() {
		if (!freshing.compareAndSet(false, true)) {
			return false;
		}
		try {
			freshing.set(true);
			Collection<T> collection = genericCacheBuilder.build();
            Map<String, T> datasTemp = null;
            if (genericCacheBuilder instanceof CacheMap) {
                datasTemp = ((CacheMap) genericCacheBuilder).getMap();
                this.threadSafety = ((CacheMap) genericCacheBuilder)
                        .threadSafety();
            } else {
                datasTemp = new HashMap<String, T>();
            }
			if (ArrayUtil.isEmpty(collection) == true) {
				log.info("load cache data failure , the data is empty!");
                datas = datasTemp;
				return false;
			}
			for (T t : collection) {
				datasTemp.put(t.getKey(), t);
			}
			datas = datasTemp;
			datasTemp = null;
			return true;
		} finally {
			freshing.set(false);
		}
	}
}
