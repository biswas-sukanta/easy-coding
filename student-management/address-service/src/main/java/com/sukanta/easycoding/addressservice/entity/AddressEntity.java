package com.sukanta.easycoding.addressservice.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class AddressEntity {
	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "uuid", updatable = false)
	private UUID id;

	@Column(name = "city")
	private String city;

	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "student_email")
	private String studentEmail;
}
