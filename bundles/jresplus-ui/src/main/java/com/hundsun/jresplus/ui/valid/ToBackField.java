package com.hundsun.jresplus.ui.valid;

import java.util.ArrayList;

import com.hundsun.jresplus.util.LabelValue;
import com.hundsun.jresplus.util.LabelValueImpl;

/**
 * 打回字段的标示
 * 
 * @author Leo
 * 
 */
public class ToBackField extends ArrayList<LabelValue<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5542428805074986194L;

	public void addBackField(String fieldName, String info) {
		add(new LabelValueImpl<String>(fieldName, info));
	}

}
