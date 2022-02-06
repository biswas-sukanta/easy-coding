package com.sukanta.easycoding.studentservice.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.sukanta.easycoding.studentservice.model.Address;
import com.sukanta.easycoding.studentservice.model.Student;

public interface StudentService extends UserDetailsService {
	Student getStudentByName(String studentFirstName);

	Student addStudent(Student student);

	ResponseEntity<Set<Address>> findAddressByEmail(String studentEmail);

	ResponseEntity<Address> addAddress(Address address);

	void addSampleData();

	Student getStudentByEmailId(String studentEmailId);
}
