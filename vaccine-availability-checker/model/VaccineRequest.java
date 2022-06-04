/**
 * 
 */
package com.covid19.vaccine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sukanta Biswas
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineRequest {

	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNo;
	private String pincode;
	private boolean isActive;
	private String ageFilter;
	private ResponseStatus response;
}
