package com.hundsun.jresplus.util.async;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hundsun.jresplus.util.async.AsyncProcessEngine.AsyncConext;

/**
 * 
 * @author Leo
 * 
 * @param <E>
 * @param <T>
 */
public class AsyncProcessQueue<E, T> {

	private final Lock lock = new ReentrantLock();
	private final Condition empty = lock.newCondition();

	private Queue<AsyncConext<E, T>> datas = new LinkedList<AsyncConext<E, T>>();
	private volatile int datasCount = 0;
	private AtomicBoolean working = new AtomicBoolean(true);

	public void addData(AsyncConext<E, T> data) {
		if (data == null) {
			return;
		}
		data.timeoutsPlus();
		lock.lock();
		try {
			datas.offer(data);
			datasCount++;
			if (working.get() && datasCount >= 1) {
				empty.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	public AsyncConext<E, T> getData() throws InterruptedException {
		lock.lock();
		try {
			while (!working.get() || datasCount == 0) {
				empty.await();
			}
			datasCount--;
			return datas.poll();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @return
	 */
	public int getQueueSize() {
		lock.lock();
		try {
			return datasCount;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @return
	 */
	public boolean getWorking() {
		return this.working.get();
	}

	public void pause() {
		lock.lock();
		try {
			this.working.set(false);
		} finally {
			lock.unlock();
		}
	}

	public void carryOn() {
		lock.lock();
		try {
			this.working.set(true);
			empty.signal();
		} finally {
			lock.unlock();
		}
	}
}
