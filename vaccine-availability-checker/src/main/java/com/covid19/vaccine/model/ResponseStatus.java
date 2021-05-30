package com.covid19.vaccine.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseStatus {
	public String statusCode;
	public String statusMsg;
}
