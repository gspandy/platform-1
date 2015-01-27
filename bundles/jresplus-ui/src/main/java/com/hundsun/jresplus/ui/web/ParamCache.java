package com.hundsun.jresplus.ui.web;

public class ParamCache {

	/**
	 * 
	 */
	private String paramId;
	private Object params;

	public ParamCache(String paramId, Object params) {
		this.paramId = paramId;
		this.params = params;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

}