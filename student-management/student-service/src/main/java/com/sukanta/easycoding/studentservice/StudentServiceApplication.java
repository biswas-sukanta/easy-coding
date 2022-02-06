package com.sukanta.easycoding.studentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.sukanta.easycoding.studentservice.controller.StudentController;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.sukanta.easycoding.studentservice.feignclients")
public class StudentServiceApplication implements CommandLineRunner {

	@Autowired
	StudentController controller;

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		controller.addSampleData();
	}
}
