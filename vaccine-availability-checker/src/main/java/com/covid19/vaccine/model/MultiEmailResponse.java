/**
 * 
 */
package com.covid19.vaccine.model;

import java.util.List;

import com.covid19.vaccine.service.model.Session;
import com.covid19.vaccine.service.model.VaccineFee;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */
@Data
public class MultiEmailResponse {

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
	public List<Session> sessions = null;
	@JsonProperty("vaccine_fees")
	public List<VaccineFee> vaccineFees = null;
}
