package com.aryasindhu.queue.capability;

import com.aryasindhu.queue.exception.InvalidCapabilityException;

/**
 * 
 * @author Aryasindhu Sahu
 *
 */
public interface DefaultQueueCapability {

	void initQueue();

	Object getQueueProducer() throws InvalidCapabilityException;

	Object getQueueConsumer() throws InvalidCapabilityException;

}
