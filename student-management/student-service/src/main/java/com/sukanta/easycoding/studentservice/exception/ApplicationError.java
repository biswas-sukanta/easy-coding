package com.sukanta.easycoding.studentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApplicationError {
	public int code;
	public String message;
}
