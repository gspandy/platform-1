package com.hundsun.jresplus.ui.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.jresplus.common.util.StringUtil;

/**
 * 
 * @author Leo
 * 
 */
public class HornHttpServletRequest extends HttpServletRequestWrapper {
	private Map<String, String[]> params = new HashMap<String, String[]>();

	public HornHttpServletRequest(HttpServletRequest request) {
		super(request);
		
			Set<Entry<String, String[]>> entries = request.getParameterMap()
					.entrySet();
			Iterator<Entry<String, String[]>> it = entries.iterator();
			while (it.hasNext()) {
				Entry<String, String[]> entry = it.next();
				if ((Boolean) request.getAttribute("emptyParamcheck")) {
					if ((entry.getValue().length == 1 && StringUtil
							.isNotEmpty(entry.getValue()[0]))) {
						params.put(entry.getKey(), entry.getValue());
					}
				}else{
					params.put(entry.getKey(), entry.getValue());
				}
				
			}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getParameterNames() {
		return new IteratorEnumeration(params.keySet().iterator());

	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return params;
	}

}
