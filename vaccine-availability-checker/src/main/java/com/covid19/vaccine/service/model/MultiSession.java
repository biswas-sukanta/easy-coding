/**
 * 
 */
package com.covid19.vaccine.service.model;

/**
 * @author Sukanta Biswas
 *
 */
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "session_id", "date", "available_capacity", "min_age_limit", "vaccine", "slots",
		"available_capacity_dose1", "available_capacity_dose2" })
public class MultiSession {
	@JsonProperty("session_id")
	public String sessionId;
	@JsonProperty("date")
	public String date;
	@JsonProperty("available_capacity")
	public Integer availableCapacity;
	@JsonProperty("min_age_limit")
	public Integer minAgeLimit;
	@JsonProperty("vaccine")
	public String vaccine;
	@JsonProperty("slots")
	public List<String> slots = null;
	@JsonProperty("available_capacity_dose1")
	public Integer availableCapacityDose1;
	@JsonProperty("available_capacity_dose2")
	public Integer availableCapacityDose2;
}