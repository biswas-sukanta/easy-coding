/**
 * 
 */
package com.learn.kafka.stream.easykafkastream.apps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import com.learn.kafka.stream.easykafkastream.model.JsonPOJODeserializer;
import com.learn.kafka.stream.easykafkastream.model.JsonPOJOSerializer;
import com.learn.kafka.stream.easykafkastream.model.Student;

/**
 * @author  Sukanta Biswas
 *
 */
@Component
public class KafkaApps {
	public void countWord() throws InterruptedException {
		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, Long> kStream = builder
				.stream("word-count-input", Consumed.with(Serdes.String(), Serdes.String()))
				.mapValues(x -> x.toUpperCase()).flatMapValues(x -> Arrays.asList(x.split(" "))).selectKey((k, v) -> v)
				.groupByKey().count(Materialized.as("count")).toStream();
		kStream.to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));
//		kStream.foreach((k, v) -> System.out.println("Data : " + k + " " + v));
		try (KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), config)) {
			kafkaStreams.start();
			Thread.sleep(900000);
		}
	}

	public void transformStudent() throws InterruptedException {

		Map<String, Object> serdeProps = new HashMap<>();
		final Serializer<Student> studentSerializer = new JsonPOJOSerializer<>();
		serdeProps.put("JsonPOJOClass", Student.class);
		studentSerializer.configure(serdeProps, false);

		final Deserializer<Student> studentDeserializer = new JsonPOJODeserializer<>();
		serdeProps.put("JsonPOJOClass", Student.class);
		studentDeserializer.configure(serdeProps, false);
		final Serde<Student> studentSerde = Serdes.serdeFrom(studentSerializer, studentDeserializer);

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "student-application");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, Student> kStream = builder.stream("student-info", Consumed.with(studentSerde, studentSerde))
				.filter((s1, s2) -> s2.getEyes().equalsIgnoreCase("black")).selectKey((k, v) -> v.getID())
				.map((k, v) -> {
					if (v.getGender().equalsIgnoreCase("0")) {
						v.setGender("Male");
					} else if (v.getGender().equalsIgnoreCase("1")) {
						v.setGender("Female");
					} else v.setGender("Unknown");
					
					return new KeyValue<String, Student>(k, v);
				});

			
		kStream.to("student-info-output", Produced.with(Serdes.String(), studentSerde));
//		kStream.foreach((k, v) -> System.out.println("Student Data : " + k + " " + v));
		KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), config);
		final CountDownLatch latch = new CountDownLatch(1);
		
		try {
			kafkaStreams.start();
			latch.await();
		} catch (Exception e) {
			System.exit(1);
		}

		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				kafkaStreams.close();
				latch.countDown();
			}
		});
	}
}
