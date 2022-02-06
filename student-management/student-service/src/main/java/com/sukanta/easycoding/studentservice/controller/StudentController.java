package com.sukanta.easycoding.studentservice.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sukanta.easycoding.studentservice.model.Address;
import com.sukanta.easycoding.studentservice.model.Student;
import com.sukanta.easycoding.studentservice.service.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class StudentController {

	@Autowired
	StudentService studentService;

	@GetMapping(path = "findStudentByFirstName/{studentFirstName}")
	public Student getStudentByName(@PathVariable String studentFirstName) {
		log.info("inside StudentController-getStudentByName()");
		return studentService.getStudentByName(studentFirstName);
	}

	@PostMapping(path = "addStudent")
	public Student addStudent(@RequestBody Student student) {
		log.info("inside StudentController-addStudent()");
		return studentService.addStudent(student);
	}

	@GetMapping(path = "findAddressByEmail/{studentEmail}")
	public ResponseEntity<Set<Address>> findAddressByEmail(@PathVariable String studentEmail) {
		log.info("inside StudentController-findAddressByEmail()");
		return studentService.findAddressByEmail(studentEmail);
	}

	@GetMapping
	public String serviceStatus() {
		return "student-service microservice is up and running";
	}

	@GetMapping(path = "postSampleData")
	public void addSampleData() {
		studentService.addSampleData();
	}

}
