package com.hundsun.jresplus.base.dict;

import java.util.List;



public interface DictStore{
	public String getDictName();
	public List<DictEntry> getAllDictData();
	public DictEntry getDictData(String label);
}