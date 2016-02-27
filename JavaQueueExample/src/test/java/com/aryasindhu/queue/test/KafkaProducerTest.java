package com.aryasindhu.queue.test;

import java.io.IOException;
import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aryasindhu.queue.impl.KafkaProducerQueue;

public class KafkaProducerTest {

	private KafkaProducerQueue producerQueue = null;
	String topicName = null;

	@BeforeClass
	public void initializeKafka() {
		producerQueue = new KafkaProducerQueue();
		topicName = "topic_1";
	}

	@Test
	public void testAddNewMessage() {
		String message = "Unique Id :" + UUID.randomUUID().toString();
		producerQueue.put(topicName, message);
	}

	@AfterClass
	public void closeResources() {
		if (producerQueue != null) {
			try {
				producerQueue.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
