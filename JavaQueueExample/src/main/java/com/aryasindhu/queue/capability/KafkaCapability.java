package com.aryasindhu.queue.capability;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import com.aryasindhu.queue.constant.CapabilityType;
import com.aryasindhu.queue.exception.InvalidCapabilityException;

/**
 * 
 * @author Aryasindhu Sahu
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class KafkaCapability<KeyType, ValueType> implements
		DefaultQueueCapability, AutoCloseable {

	private Properties kafkaProperties = null;
	private Producer<KeyType, ValueType> producer = null;
	private KafkaConsumer<KeyType, ValueType> consumer = null;
	private CapabilityType capabilityType;

	public KafkaCapability(CapabilityType capabilityType) {
		this.capabilityType = capabilityType;
	}

	private void initProducer() {
		producer = new KafkaProducer<KeyType, ValueType>(kafkaProperties);
	}

	private void initConsumer() {
		consumer = new KafkaConsumer<KeyType, ValueType>(kafkaProperties);
	}

	private void initConfig() {
		kafkaProperties = new Properties();
		try {
			kafkaProperties.load(KafkaCapability.class.getClassLoader()
					.getResourceAsStream("queue_config.properties"));
		} catch (IOException e) {
			// log.info("Config[queue_config.properties] is either Wrong or Not Found, using default config for Kafka Queue");
			e.printStackTrace();
		}
	}

	@Override
	public void initQueue() {
		initConfig();
		if (capabilityType.equals(CapabilityType.CONSUMER)) {
			initConsumer();
		} else if (capabilityType.equals(CapabilityType.PRODUCER)) {
			initProducer();
		} else if (capabilityType.equals(CapabilityType.BOTH)) {
			initConsumer();
			initProducer();
		}
	}

	@Override
	public Producer<KeyType, ValueType> getQueueProducer()
			throws InvalidCapabilityException {
		if (capabilityType.equals(CapabilityType.CONSUMER)) {
			throw new InvalidCapabilityException("Capability set for Kafka is "
					+ this.capabilityType);
		}
		if (producer == null) {
			initProducer();
		}
		return producer;
	}

	@Override
	public KafkaConsumer<KeyType, ValueType> getQueueConsumer()
			throws InvalidCapabilityException {
		if (capabilityType.equals(CapabilityType.PRODUCER)) {
			throw new InvalidCapabilityException("Capability set for Kafka is "
					+ this.capabilityType);
		}
		if (consumer == null) {
			initConsumer();
		}
		return consumer;
	}

	@Override
	public void close() throws Exception {
		if (producer != null) {
			producer.close();
			producer = null;
		} else if (consumer != null) {
			consumer.close();
			consumer = null;
		} else {
			throw new NullPointerException(
					"Producer/Consumer not yet Opened or Already closed.");
		}
	}

}
