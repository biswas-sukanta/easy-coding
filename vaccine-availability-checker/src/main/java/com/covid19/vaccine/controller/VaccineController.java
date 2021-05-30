package com.covid19.vaccine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.covid19.vaccine.model.ResponseStatus;
import com.covid19.vaccine.model.VaccineRequest;
import com.covid19.vaccine.service.VaccineChecker;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sukanta Biswas
 *
 */
@RestController
@Slf4j
public class VaccineController {

	@Autowired
	private VaccineChecker<VaccineRequest> vaccineChecker;

	@Scheduled(cron = "${cron.expression}")
	@GetMapping(path = "/checkAvailibility")
	public void checkAvailibility() {
		final List<VaccineRequest> allVaccineRecords = vaccineChecker.getAllVaccineRecords();
		log.info("Total record size : {}" + allVaccineRecords);
		allVaccineRecords.stream().filter(VaccineRequest::isActive).forEach(record -> vaccineChecker.checkAvailability(record));
	}

	@PostMapping(path = "/register")
	public ResponseEntity<ResponseStatus> register(@RequestBody VaccineRequest request) {
		log.info("incoming data - {}", request);
		if (null != vaccineChecker.registerUser(request)) {
			return new ResponseEntity<>(
					ResponseStatus.builder().statusCode("200").statusMsg("Registration Successful").build(),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				ResponseStatus.builder().statusCode("400").statusMsg("User already exists with EMail ID, Mobile No & Pincode!").build(),
				HttpStatus.BAD_REQUEST);
	}

}
