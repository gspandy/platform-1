package com.hundsun.jresplus.util.async;

/**
 * 
 * @author Leo
 * 
 */
public class AsyncProcessException extends Exception {

	private static final long serialVersionUID = -1054319599759314450L;

	public static final int TIME_OUT = 1;

	private int errorNo;

	public AsyncProcessException(Exception exception) {
		super(exception);
	}

	public AsyncProcessException(int errorNo) {
		this.errorNo = errorNo;
	}

	public int getErrorNo() {
		return this.errorNo;
	}
}
