package com.aryasindhu.queue.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.aryasindhu.queue.ConsumerQueueUtil;
import com.aryasindhu.queue.capability.KafkaCapability;
import com.aryasindhu.queue.constant.CapabilityType;
import com.aryasindhu.queue.exception.InvalidCapabilityException;

/**
 * 
 * @author Aryasindhu Sahu
 *
 */
public class KafkaConsumerQueue implements ConsumerQueueUtil<String> {

	private KafkaCapability<String, String> kafkaCapability = null;
	private KafkaConsumer<String, String> consumer = null;

	public KafkaConsumerQueue() {
		kafkaCapability = new KafkaCapability<String, String>(
				CapabilityType.CONSUMER);
		try {
			kafkaCapability.initQueue();
			consumer = kafkaCapability.getQueueConsumer();
		} catch (InvalidCapabilityException e) {
			e.printStackTrace();
		}
	}

	public KafkaConsumerQueue(String initialTopicName) {
		kafkaCapability = new KafkaCapability<String, String>(
				CapabilityType.CONSUMER);
		try {
			kafkaCapability.initQueue();
			consumer = kafkaCapability.getQueueConsumer();
			consumer.subscribe(Arrays.asList(initialTopicName));
		} catch (InvalidCapabilityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void subscribe(String topic) {
		consumer.subscribe(Arrays.asList(topic));
	}

	@Override
	public Set<String> getAll() {
		return getAll(1000);
	}

	@Override
	public Set<String> getAll(long waitMillis) {
		Set<String> messages = new HashSet<String>();
		ConsumerRecords<String, String> records = consumer.poll(1000);
		for (ConsumerRecord<String, String> record : records) {
			messages.add(record.value());
		}
		return messages;
	}

	@Override
	public void close() throws IOException {
		if (consumer != null) {
			consumer.close();
			consumer = null;
		}
	}

}

class RebalanceListener implements ConsumerRebalanceListener {

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		System.out.println("Revoked :" + partitions);
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		System.out.println("Assigned :" + partitions);
	}

}