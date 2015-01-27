package com.hundsun.jresplus.base.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.hundsun.jresplus.base.dict.DictEntry;
import com.hundsun.jresplus.base.dict.DictManager;
import com.hundsun.jresplus.beans.ObjectFactory;

public class DictManagerImpl implements DictManager ,InitializingBean{
	Logger log=LoggerFactory.getLogger(DictManagerImpl.class);
	@Autowired
	ObjectFactory factory;
	
	Map<String, DictStore> storeMap=new HashMap<String,DictStore>();
	@SuppressWarnings("unchecked")
	public <T extends DictEntry> List<T> getDicts(String dictName) {
		DictStore store=storeMap.get(dictName);
		if(store==null){
			return null;
		}
		return (List<T>)store.getAllDictData();
	}
	@SuppressWarnings("unchecked")
	public <T extends DictEntry> T getDictEntry(String dictName, String label) {
		DictStore store=storeMap.get(dictName);
		if(store==null){
			return null;
		}
		return (T)store.getDictData(label);
	}
	public void afterPropertiesSet() throws Exception {
		log.info("Default DictManagerImpl inited.");
		List<DictStore> dictStoreList=factory.getBeansOfType4List(DictStore.class);
		if(dictStoreList==null){
			return;
		}
		for(DictStore dict :dictStoreList){
			storeMap.put(dict.getDictName(), dict);
			log.info("registed DictStore [{}]-[{}]",dict.getDictName(),dict.getClass());
		}
	}

}
