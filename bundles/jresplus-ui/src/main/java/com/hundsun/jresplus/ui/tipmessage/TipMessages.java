package com.hundsun.jresplus.ui.tipmessage;

import java.util.ArrayList;

/**
 * 页面的提示项
 * 
 * @author LeoHu
 * 
 */
public class TipMessages extends ArrayList<TipMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3358625456231384787L;

	public final static String TIP_NAME = "tipMessages";

	public void add(String message) {
		super.add(new TipMessage(message));
	}

	public void addInfo(String message) {
		add(message);
	}

	public void addWarning(String message) {
		super.add(new TipMessage(TipMessage.WARNING, message));
	}

	public void addSuccess(String message) {
		super.add(new TipMessage(TipMessage.SUCCESS, message));
	}
}
