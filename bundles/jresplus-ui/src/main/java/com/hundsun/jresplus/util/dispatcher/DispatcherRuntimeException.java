package com.hundsun.jresplus.util.dispatcher;

/**
 * 任务运行时异常
 * 
 * @author LeoHu
 * 
 */
public class DispatcherRuntimeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7490575687646010541L;

	public DispatcherRuntimeException(String msg) {
		super(msg);
	}

	public DispatcherRuntimeException(Exception e) {
		super(e);
	}

	public DispatcherRuntimeException(String msg, Throwable e) {
		super(msg, e);
	}

}
