package com.hundsun.jresplus.util.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Leo
 * 
 * @param <E>
 * @param <T>
 */
@Component
@Scope("prototype")
public class AsyncProcessEngineImpl<E, T> implements AsyncProcessEngine<E, T>,
		InitializingBean {

	private static final Logger log = LoggerFactory
			.getLogger(AsyncProcessEngineImpl.class);

	private AsyncProcessQueue<E, T> asyncProcessQueue = new AsyncProcessQueue<E, T>();

	public void addTask(AsyncConext<E, T> asyncConext) {
		asyncProcessQueue.addData(asyncConext);
	}

	final Runnable runnable = new Runnable() {

		public void run() {
			for (;;) {
				try {
					AsyncConext<E, T> asyncConext = asyncProcessQueue.getData();
					try {

						if (asyncConext.getHandler().getRetryTimes() != 0
								&& asyncConext.getTimes() > asyncConext
										.getHandler().getRetryTimes()) {
							throw new AsyncProcessException(
									AsyncProcessException.TIME_OUT);
						}
						asyncConext.getHandler().processSucess(
								asyncConext.getHandler().process(
										asyncConext.getAsync()));
					} catch (AsyncProcessException e) {
						int result = asyncConext.getHandler().processException(
								asyncConext.getAsync(), e);
						if (result == AsyncProcessHandler.RETRY) {
							asyncProcessQueue.addData(asyncConext);
						}
					}

				} catch (InterruptedException e) {
					log.error(" process the task queue error!", e);
				} catch (Exception e) {
					log.error(" process the task queue error!", e);
				}
			}
		}
	};

	public void afterPropertiesSet() throws Exception {
		Thread t = new Thread(runnable);
		t.start();
	}
}
