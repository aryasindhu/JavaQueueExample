package com.aryasindhu.queue;

import java.util.Set;

/**
 * 
 * @author Aryasindhu Sahu
 *
 * @param <T>
 */
public interface ConsumerQueueUtil<T> extends QueueUtil {

	// T get(String topicName);

	void subscribe(String topic);

	// void subscribeAll(String[] topics);

	/**
	 * Waits for a maximum of waitMillis milli-seconds to receive a message
	 * 
	 * @param waitMillis
	 * @return
	 */
	Set<T> getAll(long waitMillis);

	/**
	 * Waits for a maximum of 1000ms(1 Sec) to receive a message
	 * 
	 * @return Set
	 */
	Set<T> getAll();

}
