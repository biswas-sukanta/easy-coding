package com.covid19.vaccine.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covid19.vaccine.model.ResponseStatus;
import com.covid19.vaccine.model.VaccineRequest;
import com.covid19.vaccine.service.MailService;
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

	@Autowired
	private MailService mailService;

	@Value("${vaccine.mail.frequency}")
	private int frequency;

	@Value("${spring.mail.regSub}")
	private String mailRegSub;

	@Scheduled(cron = "${cron.expression}")
	@GetMapping(path = "/checkAvailibility")
	public void checkAvailibility() {
		final List<VaccineRequest> allVaccineRecords = vaccineChecker.getAllVaccineRecords();
		log.info("Total record size : {}" + allVaccineRecords);
		allVaccineRecords.stream().filter(VaccineRequest::isActive).forEach(record -> {
			for (int i = 0; i < frequency; i++) {
				final String day = DateTimeFormatter.ofPattern("dd-MM-yyyy")
						.format(LocalDate.now().plusDays(i));
				vaccineChecker.checkAvailability(record, day);
			}
		});
	}

	@PostMapping(path = "/register")
	public ResponseEntity<ResponseStatus> register(@RequestBody VaccineRequest request) {
		log.info("incoming data - {}", request);
		if (null != vaccineChecker.registerUser(request)) {
			mailService.sendMail(request.getEmailId(), "You have been registered sucessfully!", mailRegSub);
			return new ResponseEntity<>(
					ResponseStatus.builder().statusCode("200").statusMsg("Registration Successful").build(),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				ResponseStatus.builder().statusCode("400")
						.statusMsg("User already exists with EMail ID, Mobile No & Pincode!").build(),
				HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/register")
	public ResponseEntity<ResponseStatus> register(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String emailId, @RequestParam String mobileNo, @RequestParam String pincode) {
		final VaccineRequest request = VaccineRequest.builder().firstName(firstName).lastName(lastName).emailId(emailId)
				.mobileNo(mobileNo).pincode(pincode).build();
		log.info("incoming data - {}", request);
		if (null != vaccineChecker.registerUser(request)) {
			mailService.sendMail(request.getEmailId(), "You have been registered sucessfully!", mailRegSub);
			return new ResponseEntity<>(
					ResponseStatus.builder().statusCode("200").statusMsg("Registration Successful").build(),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				ResponseStatus.builder().statusCode("400")
						.statusMsg("User already exists with EMail ID, Mobile No & Pincode!").build(),
				HttpStatus.BAD_REQUEST);
	}

}
