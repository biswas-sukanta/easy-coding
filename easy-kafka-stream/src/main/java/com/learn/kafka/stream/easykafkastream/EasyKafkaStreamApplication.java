package com.learn.kafka.stream.easykafkastream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.learn.kafka.stream.easykafkastream.apps.KafkaApps;

@SpringBootApplication
public class EasyKafkaStreamApplication implements CommandLineRunner{
	
	@Autowired
	KafkaApps count;
	public static void main(String[] args) {
		SpringApplication.run(EasyKafkaStreamApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
//		count.countWord();
		count.transformStudent();
	}

}
