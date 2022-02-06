package com.sukanta.easycoding.addressservice.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Address {

	private UUID id;
	@JsonProperty("city")
	private String city;
	@JsonProperty("pincode")
	private String pincode;
	@JsonProperty("studentEmail")
	private String studentEmail;
}
