/**
 * 
 */
package com.covid19.vaccine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */
@Data
@Entity
@Table(name = "VaccineTracker")
public class VaccineEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column(name = "first_name", nullable = false)
	public String firstName;

	@Column(name = "last_name", nullable = false)
	public String lastName;

	@Column(name = "email_id", nullable = false)
	public String emailId;

	@Column(name = "mobile_no", nullable = false)
	public String mobileNo;

	@Column(name = "pincode", nullable = false)
	public String pincode;
	
	@Column(name = "registration_date", nullable = false, updatable = false)
	public String regDate;

	@Column(name = "is_active", nullable = false)
	public boolean isActive;

}
