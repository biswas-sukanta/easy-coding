/**
 * 
 */
package com.covid19.vaccine.service.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Response {

	@JsonProperty("sessions")
	public List<Session> sessions = null;

}