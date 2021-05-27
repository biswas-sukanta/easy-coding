/**
 * 
 */
package com.covid19.vaccine.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */
@Data
public class EmailData {

	@JsonProperty("center_id")
	public int centerId;
	@JsonProperty("name")
	public String name;
	@JsonProperty("state_name")
	public String stateName;
	@JsonProperty("district_name")
	public String districtName;
	@JsonProperty("block_name")
	public String blockName;
	@JsonProperty("pincode")
	public int pincode;
	@JsonProperty("from")
	public String from;
	@JsonProperty("to")
	public String to;
	@JsonProperty("fee_type")
	public String feeType;
	@JsonProperty("date")
	public String date;
	@JsonProperty("available_capacity")
	public int availableCapacity;
	@JsonProperty("min_age_limit")
	public int minAgeLimit;
	@JsonProperty("vaccine")
	public String vaccine;
	@JsonProperty("slots")
	public List<String> slots = null;

}
