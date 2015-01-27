package com.hundsun.jresplus.base.dict;

import com.hundsun.jresplus.base.dict.DictEntry;

public class SimpleDictEntry implements DictEntry {
	private String value;
	private String label;
	public SimpleDictEntry(String code,String text){
		this.value=text;
		this.label=code;
	}
	public SimpleDictEntry(){}
	public String getText() {
		return value;
	}

	public void setText(String text) {
		this.value = text;
	}

	public String getCode() {
		return label;
	}

	public void setCode(String code) {
		this.label = code;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

}
