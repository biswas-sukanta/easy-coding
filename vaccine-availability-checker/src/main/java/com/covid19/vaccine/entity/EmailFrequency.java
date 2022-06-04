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
@Table(name = "EmailFrequencey")
public class EmailFrequency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column(name = "hour", nullable = false)
	public int hour;

	@Column(name = "email_id", nullable = false)
	public String emailId;
	
	@Column(name = "password", nullable = false)
	public String password;
}
