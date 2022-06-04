/**
 * 
 */
package com.covid19.vaccine.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "center_id", "name", "address", "state_name", "district_name", "block_name", "pincode", "lat",
		"long", "from", "to", "fee_type", "sessions", "vaccine_fees" })
public class Center {

	@JsonProperty("center_id")
	public Integer centerId;
	@JsonProperty("name")
	public String name;
	@JsonProperty("address")
	public String address;
	@JsonProperty("state_name")
	public String stateName;
	@JsonProperty("district_name")
	public String districtName;
	@JsonProperty("block_name")
	public String blockName;
	@JsonProperty("pincode")
	public Integer pincode;
	@JsonProperty("lat")
	public Integer lat;
	@JsonProperty("long")
	public Integer _long;
	@JsonProperty("from")
	public String from;
	@JsonProperty("to")
	public String to;
	@JsonProperty("fee_type")
	public String feeType;
	@JsonProperty("sessions")
	public List<MultiSession> multiSessions;
	@JsonProperty("vaccine_fees")
	public List<VaccineFee> vaccineFees;
}