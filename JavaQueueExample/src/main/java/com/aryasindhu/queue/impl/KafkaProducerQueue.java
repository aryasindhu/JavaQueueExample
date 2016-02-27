package com.aryasindhu.queue.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.aryasindhu.queue.ProducerQueueUtil;
import com.aryasindhu.queue.capability.KafkaCapability;
import com.aryasindhu.queue.constant.CapabilityType;
import com.aryasindhu.queue.exception.InvalidCapabilityException;

/**
 * A Kafka Distributed Queue Client Implementation for String Data queuing. It uses
 * configurations from [queue_config.properties] from classpath.
 * 
 * @author Aryasindhu Sahu
 *
 */
public class KafkaProducerQueue implements ProducerQueueUtil<String> {

	private KafkaCapability<String, String> kafkaCapability = null;
	private Producer<String, String> producer = null;
	private Callback callback = null;

	public KafkaProducerQueue(Callback messagePushListener) {
		this.callback = messagePushListener;
		initKafkaQueue(callback);
	}

	public KafkaProducerQueue() {
		this.callback = new MessagePushListener();
		initKafkaQueue(callback);
	}

	private void initKafkaQueue(Callback messagePushListener) {
		kafkaCapability = new KafkaCapability<String, String>(
				CapabilityType.PRODUCER);
		try {
			kafkaCapability.initQueue();
			producer = kafkaCapability.getQueueProducer();
		} catch (InvalidCapabilityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void put(String topicName, String message) {
		this.producer.send(new ProducerRecord<String, String>(topicName,
				message), this.callback);
	}

	@Override
	public void putAllSingleMessages(Map<String, String> messages) {
		for (String topicName : messages.keySet()) {
			put(topicName, messages.get(topicName));
		}
	}

	@Override
	public void putAll(Map<String, Set<String>> messages) {
		for (String topicName : messages.keySet()) {
			for (String message : messages.get(topicName)) {
				put(topicName, message);
			}
		}
	}

	@Override
	public void putAll(String topicName, Set<String> messages) {
		for (String message : messages) {
			put(topicName, message);
		}
	}

	@Override
	public void close() throws IOException {
		if (producer != null) {
			producer.close();
			producer = null;
		}
	}
}

class MessagePushListener implements Callback {

	@Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		if (metadata != null) {
			System.out.println("Data Push Comeplete, offset["
					+ metadata.offset() + "]");
		} else {
			exception.printStackTrace();
			throw new RuntimeException("Error sending data to server :",
					exception);
		}
	}
}