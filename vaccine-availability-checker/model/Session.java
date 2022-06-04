/**
 * 
 */
package com.covid19.vaccine.model;

/**
 * @author Sukanta Biswas
 *
 */
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "center_id", "name", "state_name", "district_name", "block_name", "pincode", "from", "to", "lat",
		"long", "fee_type", "session_id", "date", "available_capacity", "fee", "min_age_limit", "vaccine", "slots" })

@Data
public class Session {

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
	@JsonProperty("lat")
	public int lat;
	@JsonProperty("long")
	public int _long;
	@JsonProperty("fee_type")
	public String feeType;
	@JsonProperty("session_id")
	public String sessionId;
	@JsonProperty("date")
	public String date;
	@JsonProperty("available_capacity")
	public int availableCapacity;
	@JsonProperty("available_capacity_dose1")
	public int firstDosageCapacity;
	@JsonProperty("available_capacity_dose2")
	public int secondDosageCapacity;
	@JsonProperty("fee")
	public String fee;
	@JsonProperty("min_age_limit")
	public int minAgeLimit;
	@JsonProperty("vaccine")
	public String vaccine;
	@JsonProperty("slots")
	public List<String> slots = null;

}*/

//Multi Response Code
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "session_id", "date", "available_capacity", "min_age_limit", "vaccine", "slots",
		"available_capacity_dose1", "available_capacity_dose2" })
public class Session {
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