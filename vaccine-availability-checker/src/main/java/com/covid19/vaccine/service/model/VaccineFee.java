/**
 * 
 */
package com.covid19.vaccine.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "vaccine", "fee" })
public class VaccineFee {

	@JsonProperty("vaccine")
	public String vaccine;
	@JsonProperty("fee")
	public String fee;
}