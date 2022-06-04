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
@JsonPropertyOrder({ "centers" })
public class MultiResponse {
	@JsonProperty("centers")
	public List<Center> centers;
}
