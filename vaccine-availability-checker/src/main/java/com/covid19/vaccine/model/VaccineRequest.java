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
	
	private String name;
	private String emailId;
	private String mobileNo;
	private String pinCode;
	private String date;
}
