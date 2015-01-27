package com.hundsun.jresplus.base.dict;

import java.util.List;

/**
 * UI字典的接口，如果想在UI上使用自动的字典处理，请实现该接口
 * 
 * @author Leo
 * 
 */
public interface DictManager {

	/**
	 * 返回指定字典名的所有字典项
	 * 
	 * @param dictName
	 *            在页面上使用的字典名
	 * @return
	 */
	public <T extends DictEntry> List<T> getDicts(String dictName);

	/**
	 * 返回单个字典名的某个字典项
	 * 
	 * @param dictName
	 * @param label
	 * @return
	 */
	public <T extends DictEntry> T getDictEntry(String dictName, String label);
}
