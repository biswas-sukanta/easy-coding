/**
 * 
 */
package com.covid19.vaccine.service;

import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.covid19.vaccine.entity.VaccineEntity;
import com.covid19.vaccine.model.EmailData;
import com.covid19.vaccine.model.VaccineRequest;
import com.covid19.vaccine.repository.VaccineRepository;
import com.covid19.vaccine.service.model.Center;
import com.covid19.vaccine.service.model.MultiResponse;
import com.covid19.vaccine.service.model.MultiSession;
import com.covid19.vaccine.service.model.Response;
import com.covid19.vaccine.util.CommonConstants;
import com.covid19.vaccine.util.UtilityClass;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sukanta Biswas
 *
 */

@Service
@Slf4j
public class VaccineCheckerImpl implements VaccineChecker<VaccineRequest> {

	@Autowired
	private VaccineRepository repository;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MailService mailService;

	@Value("${vaccine.mail.notification}")
	private String emailNotification;
	
	@Value("${vaccine.mail.registration}")
	private String emailRegistration;

	@Value("${apisetu.endpoint.findByPin}")
	private String findByPinUrl;
	
	@Value("${apisetu.endpoint.findByPinMulti}")
	private String findByPinMulti;
	
	@Value("${spring.mail.mainSub}")
	private String mailMainSub;
	
	@Value("${spring.mail.multiMailSub}")
	private String multiMailSub;
	

	@Override
	public void checkAvailability(VaccineRequest vaccineRequest, String day) {

		try {
			final String url = findByPinUrl.replace(CommonConstants.PLACEHOLDER_PIN, String.valueOf(vaccineRequest.getPincode())).replace(CommonConstants.PLACEHOLDER_DATE, day);

			log.info("API-SETU-URL - {}", url);

			final HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			ResponseEntity<Response> responseObj = null;

			try {
				responseObj = restTemplate.exchange(url, HttpMethod.GET, entity, Response.class);
			} catch (final RestClientException e) {
				log.info("Exception while invoking apisetu url : {}", e);
			}

			log.info("Response : {}", responseObj);

			if (null != responseObj && null != responseObj.getBody()) {
				final List<EmailData> emailDatas = new ArrayList<>();
				responseObj.getBody().getSessions().forEach(session -> {
					final EmailData emailData = modelMapper.map(session, EmailData.class);

					if (emailData.getAvailableCapacity() > 0) {
						if (null == vaccineRequest.getAgeFilter() || UtilityClass.isBlank(vaccineRequest.getAgeFilter()) || vaccineRequest.getAgeFilter().equalsIgnoreCase(null)) {
							emailDatas.add(emailData);
						} else {
							if (vaccineRequest.getAgeFilter().equalsIgnoreCase(String.valueOf(emailData.getMinAgeLimit())))
								emailDatas.add(emailData);
						}
					}
				});

				if (!emailDatas.isEmpty()) {
					final StringWriter writer = settingEmailTemplate(emailDatas);
					mailService.sendMail(vaccineRequest.getEmailId(), writer.toString(), mailMainSub.replace(CommonConstants.PLACEHOLDER_DATE, day));
				}
			}
		} catch (Exception e) {
			log.error("VaccineCheckerImpl.checkAvailability() : {}", e);
		}
	}
	
	
	@Override
	public void checkMultiAvailability(VaccineRequest vaccineRequest, String day) {

		try {
			final String url = findByPinMulti.replace(CommonConstants.PLACEHOLDER_PIN , String.valueOf(vaccineRequest.getPincode())).replace(CommonConstants.PLACEHOLDER_DATE, day);

			log.info("API-SETU-URL - {}", url);

			final HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			ResponseEntity<MultiResponse> responseObj = null;

			try {
				responseObj = restTemplate.exchange(url, HttpMethod.GET, entity, MultiResponse.class);
			} catch (final RestClientException e) {
				log.info("Exception while invoking apisetu url : {}", e);
			}

			log.info("Response : {}", responseObj);

			if (null != responseObj && null != responseObj.getBody() && !responseObj.getBody().getCenters().isEmpty()) {
				List<EmailData> emailDatas = new ArrayList<>();
				
				responseObj.getBody().getCenters().forEach(center -> {

					if (null != center.getVaccineFees() && !center.getVaccineFees().isEmpty()) {

						center.getMultiSessions().stream().filter(sessionData -> sessionData.getAvailableCapacity() > 0)
								.forEach(sessionData -> {
									if (((UtilityClass.isBlank(vaccineRequest.getAgeFilter()) && UtilityClass.isBlank(vaccineRequest.getFeeType()) && UtilityClass.isBlank(vaccineRequest.getVaccineName()))
											|| (UtilityClass.isBlank(vaccineRequest.getAgeFilter()) && checkFeeType(vaccineRequest, center) && UtilityClass.isBlank(vaccineRequest.getVaccineName()))
											|| (UtilityClass.isBlank(vaccineRequest.getAgeFilter()) && UtilityClass.isBlank(vaccineRequest.getFeeType()) && checkVaccineName(vaccineRequest, sessionData))
											|| (UtilityClass.isBlank(vaccineRequest.getAgeFilter()) && checkFeeType(vaccineRequest, center) && checkVaccineName(vaccineRequest, sessionData))
											|| (checkAgeFilter(vaccineRequest, sessionData) && UtilityClass.isBlank(vaccineRequest.getFeeType()) && UtilityClass.isBlank(vaccineRequest.getVaccineName()))
											|| (checkAgeFilter(vaccineRequest, sessionData) && UtilityClass.isBlank(vaccineRequest.getFeeType()) && checkVaccineName(vaccineRequest, sessionData))
											|| (checkAgeFilter(vaccineRequest, sessionData) && checkFeeType(vaccineRequest, center) && UtilityClass.isBlank(vaccineRequest.getVaccineName())))
											|| (vaccineRequest.getAgeFilter().equalsIgnoreCase(String.valueOf(sessionData.getMinAgeLimit())) 
													&& vaccineRequest.getFeeType().equalsIgnoreCase(center.getFeeType())
													&& vaccineRequest.getVaccineName().equalsIgnoreCase(sessionData.getVaccine()))) {
										settingEmailData(emailDatas, center, sessionData);
									}
								});
					}
				});

				List<EmailData> distinctEmaildatas = emailDatas.stream().distinct().sorted(Comparator.comparing(EmailData::getDate)).collect(Collectors.toList());
				log.info("Data : {}", distinctEmaildatas);

				if (!emailDatas.isEmpty()) {
					final StringWriter writer = settingEmailTemplate(distinctEmaildatas);
					mailService.sendMail(vaccineRequest.getEmailId(), writer.toString(), multiMailSub.replace("#pin#", vaccineRequest.getPincode()));
				}
			}
		} catch (Exception e) {
			log.error("VaccineCheckerImpl.checkAvailability() : {}", e);
		}
	}
	
	
	private boolean checkAgeFilter(final VaccineRequest vaccineRequest, final MultiSession sessionData) {
		return !UtilityClass.isBlank(vaccineRequest.getAgeFilter())
				&& vaccineRequest.getAgeFilter().equalsIgnoreCase(String.valueOf(sessionData.getMinAgeLimit()));
	}

	private boolean checkFeeType(final VaccineRequest vaccineRequest, final Center center) {
		return !UtilityClass.isBlank(vaccineRequest.getFeeType())
				&& vaccineRequest.getFeeType().equalsIgnoreCase(String.valueOf(center.getFeeType()));
	}

	private boolean checkVaccineName(final VaccineRequest vaccineRequest, final MultiSession sessionData) {
		return !UtilityClass.isBlank(vaccineRequest.getVaccineName())
				&& vaccineRequest.getVaccineName().equalsIgnoreCase(String.valueOf(sessionData.getVaccine()));
	}


	/**
	 * @param emailData
	 * @param emailDatas
	 * @param multiEmailData
	 * @param curSession
	 */
	private void settingEmailData(List<EmailData> emailDatas,
			Center multiEmailData, MultiSession curSession) {
		EmailData emailData = new EmailData();
		emailData.setCenterId(multiEmailData.getCenterId());
		emailData.setName(multiEmailData.getName());
		emailData.setStateName(multiEmailData.getStateName());
		emailData.setDistrictName(multiEmailData.getDistrictName());
		emailData.setBlockName(multiEmailData.getBlockName());
		emailData.setPincode(multiEmailData.getPincode());
		emailData.setFrom(multiEmailData.getFrom());
		emailData.setTo(multiEmailData.getTo());
		emailData.setFeeType(multiEmailData.getFeeType());
		emailData.setDate(curSession.getDate());
		emailData.setAvailableCapacity(curSession.getAvailableCapacity());
		emailData.setFirstDosageCapacity(curSession.getAvailableCapacityDose1());
		emailData.setSecondDosageCapacity(curSession.getAvailableCapacityDose2());
		emailData.setMinAgeLimit(curSession.getMinAgeLimit());
		emailData.setVaccine(curSession.getVaccine());
		emailData.setSlots(curSession.getSlots());
		emailDatas.add(emailData);
	}

	@Override
	public StringWriter settingEmailTemplate(final List<EmailData> emailDatas) {

		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init();
		log.info("template path : {}", emailNotification);
		Template t = null;
		final VelocityContext context = new VelocityContext();
		final StringWriter writer = new StringWriter();
		if (null != emailDatas) {
			context.put("emailDatas", emailDatas);
			t = velocityEngine.getTemplate(emailNotification);
		} else {
			t = velocityEngine.getTemplate(emailRegistration);
		}
		t.merge(context, writer);
		return writer;
	}

	@Override
	public List<VaccineRequest> getAllVaccineRecords() {
		final List<VaccineRequest> records = repository.findAll().stream().sorted(Comparator.comparing(VaccineEntity::getId).reversed())
				.map(record -> modelMapper.map(record, VaccineRequest.class)).collect(Collectors.toList());
		return records.isEmpty() ? Collections.emptyList() : records;
	}

	@Override
	public VaccineEntity registerUser(VaccineRequest record) {
		if (!repository.findByEmailIdAndMobileNoAndPincodeAndAgeFilterAndIsActive(record.getEmailId(), record.getMobileNo(),
				record.getPincode(), record.getAgeFilter(), Boolean.TRUE).isEmpty()) {
			log.info("User already Exists!");
			return null;
		}
		final VaccineEntity vaccineEntity = modelMapper.map(record, VaccineEntity.class);
		final String today = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDate());
		vaccineEntity.setRegDate(today);
		vaccineEntity.setActive(true);
		return repository.save(vaccineEntity);
	}

}
