package com.sukanta.easycoding.studentservice.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.sukanta.easycoding.studentservice.model.Address;

public interface StudentFallbackService {
	ResponseEntity<Set<Address>> findAddressByEmailfallback(String studentEmail, Throwable throwable);
}
