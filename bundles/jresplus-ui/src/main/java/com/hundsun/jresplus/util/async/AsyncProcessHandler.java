package com.hundsun.jresplus.util.async;

/**
 * 
 * @author Leo
 * 
 * @param <E>
 * @param <T>
 */
public interface AsyncProcessHandler<E, T> {

	public static final int RETRY = 0;
	public static final int GIVEUP = 1;

	public E process(T async) throws AsyncProcessException;

	public void processSucess(E result);

	public int processException(T async, AsyncProcessException exception);

	public int getRetryTimes();

}
