package com.aryasindhu.queue;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Aryasindhu Sahu
 *
 * @param <T>
 */
public interface ProducerQueueUtil<T> extends QueueUtil {

	void put(String topicName, T message);

	void putAllSingleMessages(Map<String, T> message);

	void putAll(Map<String, Set<T>> message);

	void putAll(String topicName, Set<String> message);

}
