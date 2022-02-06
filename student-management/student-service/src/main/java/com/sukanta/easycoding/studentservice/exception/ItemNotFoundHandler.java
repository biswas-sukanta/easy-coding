package com.sukanta.easycoding.studentservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
public class ItemNotFoundHandler extends ResponseEntityExceptionHandler {

	Logger log = LoggerFactory.getLogger(ItemNotFoundHandler.class);

	@ExceptionHandler(value = ItemNotFoundException.class)
	public ResponseEntity<ApplicationError> handleItemNotFoundException(ItemNotFoundException exception,
			WebRequest webRequest) {
		MDC.put("error.status.code", String.valueOf(HttpStatus.NOT_FOUND.value()));
		log.error("WebRequest : {}, Error Message : {}", webRequest, exception.getMessage());
		MDC.clear();
		return new ResponseEntity<>(ApplicationError.builder().code(HttpStatus.NOT_FOUND.value()).message(exception.getMessage()).build(),
				HttpStatus.NOT_FOUND);
	}
}