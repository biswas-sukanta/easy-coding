package com.sukanta.easycoding.addressservice.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sukanta.easycoding.addressservice.model.Address;
import com.sukanta.easycoding.addressservice.service.AddressService;

@RestController
public class AddressController {

	@Autowired
	AddressService addressService;

	Logger logger = LoggerFactory.getLogger(AddressController.class);

	@GetMapping(path = "findAddressByEmail/{studentEmail}")
	public ResponseEntity<Set<Address>> findAddressByEmail(@PathVariable String studentEmail) {
		logger.info("inside AddressController-findAddressByEmail()");
		return new ResponseEntity<>(addressService.findAddressByEmail(studentEmail), HttpStatus.OK);
	}

	@PostMapping(path = "addAddress")
	public ResponseEntity<Address> addAddress(@RequestBody Address address) {
		logger.info("inside AddressController-addAddress()");
		return new ResponseEntity<>(addressService.addAddress(address), HttpStatus.CREATED);
	}

	public String serviceStatus() {
		return "address-service microservice is up and running";
	}
}
