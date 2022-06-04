package com.covid19.vaccine.controller;

import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import com.covid19.vaccine.entity.VaccineEntity;
import com.covid19.vaccine.model.ResponseStatus;
import com.covid19.vaccine.model.VaccineRequest;
import com.covid19.vaccine.service.MailService;
import com.covid19.vaccine.service.VaccineChecker;
import com.covid19.vaccine.util.CommonConstants;

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

	@Value("${app.api.password}")
	private String appApiPassword;
	
	@Value("${app.api.sevenDaysAvailabilityOn}")
	private boolean sevenDaysAvailabilityOn;

	@Scheduled(cron = "${cron.expression}", zone = "IST")
	@GetMapping(path = "/checkAvailibility")
	public void checkAvailibility() {
		final List<VaccineRequest> allVaccineRecords = vaccineChecker.getAllVaccineRecords();
		log.info("Total record size : {}", allVaccineRecords.size());

		final ExecutorService exService = Executors.newFixedThreadPool(10);
		final ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		log.info("Availability Check is started");
		CompletableFuture.runAsync(() -> allVaccineRecords.stream().filter(VaccineRequest::isActive).forEach(record -> {

			if (sevenDaysAvailabilityOn) {
				vaccineChecker.checkMultiAvailability(record, dateFormatter.format(zdt.toLocalDate()));
				return;
			}
			for (long i = 0; i < frequency; i++) {
				final String day = dateFormatter.format(zdt.toLocalDate().plusDays(i));
				vaccineChecker.checkAvailability(record, day);
			}
		}), exService);
	}

	@PostMapping(path = "/register")
	public ResponseEntity<ResponseStatus> register(@RequestBody VaccineRequest request) {
		log.info("incoming data - {}", request);
		if (null != vaccineChecker.registerUser(request)) {
			mailService.sendMail(request.getEmailId(), CommonConstants.REGISTRATION_MSG, mailRegSub);
			return new ResponseEntity<>(ResponseStatus.builder().statusCode(CommonConstants.STATUS_CODE_200)
					.statusMsg(CommonConstants.SUCCESS_MSG).build(), HttpStatus.OK);
		}
		return new ResponseEntity<>(ResponseStatus.builder().statusCode(CommonConstants.STATUS_CODE_400)
				.statusMsg(CommonConstants.USER_EXISTS_MSG).build(), HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/register")
	public ResponseEntity<ResponseStatus> register(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String emailId, @RequestParam String mobileNo, @RequestParam String pincode,
			@RequestParam(required = false) String ageFilter, @RequestParam(required = false) String feeType,
			@RequestParam(required = false) String vaccineName) {
		
		boolean success = false;
		try {
			String[] pincodes = pincode.split(",");
			for (String pin : pincodes) {
				final VaccineRequest request = VaccineRequest.builder().firstName(firstName).lastName(lastName).emailId(emailId)
						.mobileNo(mobileNo).pincode(pin).ageFilter(ageFilter).feeType(feeType).vaccineName(vaccineName).build();
				log.info("incoming data - {}", request);
				VaccineEntity registerUser = vaccineChecker.registerUser(request);
				if (null != registerUser) {
					success = true;
				}
			}
		} catch (Exception e) {
			log.error("Error @ register()", e);
			return new ResponseEntity<>(ResponseStatus.builder().statusCode(CommonConstants.STATUS_CODE_400)
					.statusMsg(CommonConstants.SERVER_ERROR).build(), HttpStatus.BAD_REQUEST);
		}
		if (success) {
			final StringWriter emailTemplate = vaccineChecker.settingEmailTemplate(null);
			mailService.sendMail(emailId, emailTemplate.toString(), mailRegSub);
			return new ResponseEntity<>(ResponseStatus.builder().statusCode(CommonConstants.STATUS_CODE_200)
					.statusMsg(CommonConstants.SUCCESS_MSG).build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(ResponseStatus.builder().statusCode(CommonConstants.STATUS_CODE_400)
					.statusMsg(CommonConstants.USER_EXISTS_MSG).build(), HttpStatus.BAD_REQUEST);
		}
		
	}

	@GetMapping(path = "/getAllRecords")
	public ResponseEntity<List<VaccineRequest>> getAllRecords(@RequestParam String apiPassword) {
		return apiPassword.equals(appApiPassword)
				? new ResponseEntity<>(vaccineChecker.getAllVaccineRecords(), HttpStatus.OK)
				: new ResponseEntity<>(Collections.emptyList(), HttpStatus.FORBIDDEN);
	}
	
	@GetMapping(path = "/getZonalTime")
	public ResponseEntity<String> getAllRecords() {
		ZonedDateTime defaultZoneTime = ZonedDateTime.now(ZoneId.systemDefault());
		ZonedDateTime specificZone = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		return new ResponseEntity<>("Default System Time - " + defaultZoneTime.toString() + " Kolkata Time - " + specificZone.toString(), HttpStatus.OK);
	}
}
