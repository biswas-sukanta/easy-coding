/**
 * 
 */
package com.covid19.vaccine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.covid19.vaccine.model.VaccineRequest;
import com.covid19.vaccine.service.VaccineChecker;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sukanta Biswas
 *
 */
@RestController
@Slf4j
public class PrimeController {

	@Autowired
	private VaccineChecker vaccineChecker;

	@PostMapping(path = "/register")
	public String register(@RequestBody VaccineRequest request) {
		log.info("incoming data - {}", request);
		vaccineChecker.checkAvailability(request);
		return "Registered Successfully";
	}
}
