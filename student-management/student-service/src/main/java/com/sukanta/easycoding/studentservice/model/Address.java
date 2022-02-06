package com.sukanta.easycoding.studentservice.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
	private UUID id;
	@JsonProperty("city")
	private String city;
	@JsonProperty("pincode")
	private String pincode;
	@JsonProperty("studentEmail")
	private String studentEmail;
}
