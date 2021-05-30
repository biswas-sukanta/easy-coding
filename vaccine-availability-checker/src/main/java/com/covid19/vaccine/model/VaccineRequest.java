/**
 * 
 */
package com.covid19.vaccine.model;

import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */
@Data
public class VaccineRequest {

	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNo;
	private String pincode;
	private String regDate;
	public boolean isActive;
	private ResponseStatus response;
}
