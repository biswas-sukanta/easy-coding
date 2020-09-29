package com.sukanta.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sukanta.app.model.CaptchaResponse;
import com.sukanta.app.util.RecaptchaUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaptchaValidatorService {

	private Logger logger = LoggerFactory.getLogger(CaptchaValidatorService.class);

	private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

	public boolean validateCaptcha(MultiValueMap<String, String> requestMap) {
		RestTemplate restTemplate = new RestTemplate();

		CaptchaResponse apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap,
				CaptchaResponse.class);
		logger.info("Captcha api response {}", apiResponse);

		if (!apiResponse.getSuccess()) {
			List<String> errorCodes = apiResponse.getErrorCodes();
			String errorMessage = errorCodes.stream().map(s -> RecaptchaUtil.RECAPTCHA_ERROR_CODE.get(s))
					.collect(Collectors.joining(", "));
			log.error("error message : " + errorMessage);
		}

		return apiResponse.getSuccess();
	}

}
