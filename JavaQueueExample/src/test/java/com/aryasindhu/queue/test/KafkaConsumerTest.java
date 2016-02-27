package com.aryasindhu.queue.test;

import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aryasindhu.queue.impl.KafkaConsumerQueue;

public class KafkaConsumerTest {

	private KafkaConsumerQueue consumerQueue = null;
	String topicName = null;

	@BeforeClass
	public void initializeKafka() {
		topicName = "topic_1";
		consumerQueue = new KafkaConsumerQueue();
		consumerQueue.subscribe(topicName);
	}

	@Test
	public void testGetMessage() {
		String ExpectedMessage = "Unique Id";
		Set<String> messages = consumerQueue.getAll();
		for(String msg : messages) {
			Assert.assertTrue(msg.startsWith(ExpectedMessage));			
		}
		Assert.assertFalse("No Msg received.", messages.isEmpty());
	}
	
	@AfterClass
	public void closeResources() {
		if (consumerQueue != null) {
			try {
				consumerQueue.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
