package com.sukanta.app.aspect;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sukanta.app.exception.BadRequestException;
import com.sukanta.app.service.CaptchaValidatorService;

@Aspect
@Component
public class CaptchaAspect {

    @Autowired
    private CaptchaValidatorService captchaValidator;
    
	@Value("${google.recaptcha.secret}")
	private String recaptchaSecret;

//    private static final String CAPTCHA_HEADER_NAME = "captcha-response";

    @Around("@annotation(RequiresCaptcha)")
    public Object validateCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String captchaResponse = request.getHeader(CAPTCHA_HEADER_NAME);
        String captchaResponse = null;
        
        Optional<String> findFirst = Arrays.asList(request.getParameterValues("g-recaptcha-response")).stream().filter(StringUtils::isNoneBlank).findFirst();
        
        if(findFirst.isPresent()){
        	captchaResponse = findFirst.get();
        }else{
        	throw new BadRequestException("Invalid captcha");
        }
        
		MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
		requestMap.add("secret", recaptchaSecret);
		requestMap.add("response", captchaResponse);
		requestMap.add("ip", request.getRemoteAddr());
		
        boolean isValidCaptcha = captchaValidator.validateCaptcha(requestMap);
        
        if(!isValidCaptcha){
            throw new BadRequestException("Invalid captcha");
        }
        return joinPoint.proceed();
    }

}
