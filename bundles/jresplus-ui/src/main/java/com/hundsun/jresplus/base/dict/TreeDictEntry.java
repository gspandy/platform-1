package com.hundsun.jresplus.base.dict;

/**
 * 树形的字典模型，例如省和市，实现后可以在UI上做到自动联动
 * 
 * @author Leo
 * 
 */
public interface TreeDictEntry extends DictEntry {

	public String getPLabel();
}
