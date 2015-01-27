package com.hundsun.jresplus.util.async;

/**
 * 
 * @author Leo
 * 
 * @param <E>
 * @param <T>
 */
public interface AsyncProcessEngine<E, T> {

	public void addTask(AsyncConext<E, T> taskConext);

	public final class AsyncConext<E, T> {

		private T async;
		private int times = 0;
		private AsyncProcessHandler<E, T> handler;

		public AsyncConext(T async, AsyncProcessHandler<E, T> handler) {
			this.async = async;
			this.handler = handler;
		}

		public T getAsync() {
			return async;
		}

		public void setAsync(T async) {
			this.async = async;
		}

		public AsyncProcessHandler<E, T> getHandler() {
			return handler;
		}

		public void setHandler(AsyncProcessHandler<E, T> handler) {
			this.handler = handler;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}

		public void timeoutsPlus() {
			this.times++;
		}
	}
}
