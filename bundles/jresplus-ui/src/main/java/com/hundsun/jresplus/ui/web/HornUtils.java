package com.hundsun.jresplus.ui.web;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.hundsun.jresplus.base.dict.DictEntry;
import com.hundsun.jresplus.base.dict.DictManager;
import com.hundsun.jresplus.common.util.StringUtil;
import com.hundsun.jresplus.web.nosession.RandomShortUUID;
import com.hundsun.jresplus.web.nosession.UUIDGenerator;

/**
 * 
 * @author Leo
 * 
 */
public class HornUtils {

	HttpServletRequest request;

	private DictManager dictManager;

	private final static UUIDGenerator uuidGenerator = new RandomShortUUID();

	public HornUtils(HttpServletRequest request, DictManager dictManager) {
		this.request = request;
		this.dictManager = dictManager;
	}

	public String parseDictValue(String dictName, String label) {
		if(dictManager==null){
			return "";
		}
		DictEntry entry = dictManager.getDictEntry(dictName, label);
		return entry == null ? "" : entry.getValue();
	}

	public String getGroupStr(String name, String group) {
		String groupStr = (String) request.getAttribute(name + ".checkGroup");
		return (StringUtil.isNotEmpty(group) ? group + ";" : "")
				.concat(null != groupStr ? groupStr : "");
	}

	public String getCheckStr(String name, String check) {
		String checkStr = (String) request.getAttribute(name + ".checkStr");
		return (StringUtil.isNotEmpty(check) ? check + ";" : "")
				.concat(null != checkStr ? checkStr : "");
	}

	/**
	 * 把一个name或者是user.name的名字转换成name[0]或user[0].name
	 * @param name
	 * @param idx
	 * @return
	 */
	public static String parseNameAsListEntry(String name, int idx) {
		if (name == null)
			return "";
		String[] nameArr = name.split("\\.");
		return nameArr.length == 2 ? nameArr[0] + "[" + idx + "]." + nameArr[1]
				: name + "[" + idx + "]";
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void addGlobal(String key, Object value) {
		Object obj = this.request.getAttribute(key);
		Set<Object> set = null;
		if (obj == null) {
			set = new LinkedHashSet<Object>();
			set.add(value);
			this.request.setAttribute(key, set);
			return;
		}
		if (!(obj instanceof Set)) {
			set = new LinkedHashSet<Object>();
			set.add(obj);
			set.add(value);
			this.request.setAttribute(key, set);
			return;
		}
		set = (Set<Object>) obj;
		set.add(value);
	}

	public ParamCacheRender printParamCaches() {
		return new ParamCacheRender(this.request);
	}

	public ParamIdRender paramsCache(Object object) {
		String uuid = getUUID();
		addGlobal("paramCaches", new ParamCache( uuid, object));
		return new ParamIdRender(uuid);
	}
	
    public ValueRender getValue(String name) {
        return new ValueRender( name);
    }
    
    public static String getUUID() {
		return uuidGenerator.gain();
	}

}
