/**
 * 
 */
package com.covid19.vaccine.service;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.covid19.vaccine.model.Response;
import com.covid19.vaccine.model.VaccineRequest;
import com.covid19.vaccine.repository.VaccineRepository;

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

	@Value("${vaccine.mail.template}")
	private String emailTemplate;

	@Value("${apisetu.endpoint.findByPin}")
	private String findByPinUrl;
	
	@Value("${spring.mail.mainSub}")
	private String mailMainSub;

	@Override
	public void checkAvailability(VaccineRequest vaccineRequest, String day) {

		final String url = findByPinUrl.replace("#pin#", String.valueOf(vaccineRequest.getPincode())).replace("#date#", day);

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
					emailDatas.add(emailData);
				}
			});

			if (!emailDatas.isEmpty()) {

				final VelocityEngine velocityEngine = new VelocityEngine();
				velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
				velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				velocityEngine.init();
				log.info("template path : {}", emailTemplate);
				final Template t = velocityEngine.getTemplate(emailTemplate);
				final VelocityContext context = new VelocityContext();
				context.put("emailDatas", emailDatas);

				final StringWriter writer = new StringWriter();
				t.merge(context, writer);
				mailService.sendMail(vaccineRequest.getEmailId(), writer.toString(), mailMainSub);
			}
		}
	}

	@Override
	public List<VaccineRequest> getAllVaccineRecords() {
		final List<VaccineRequest> records = repository.findAll().stream()
				.map(record -> modelMapper.map(record, VaccineRequest.class)).collect(Collectors.toList());
		return records.isEmpty() ? Collections.emptyList() : records;
	}

	@Override
	public VaccineEntity registerUser(VaccineRequest record) {
		if (!repository
				.findByEmailIdAndMobileNoAndPincode(record.getEmailId(), record.getMobileNo(), record.getPincode())
				.isEmpty()) {
			return null;
		}
		final VaccineEntity vaccineEntity = modelMapper.map(record, VaccineEntity.class);
		final String today = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now());
		vaccineEntity.setRegDate(today);
		vaccineEntity.setActive(true);
		return repository.save(vaccineEntity);
	}

}
