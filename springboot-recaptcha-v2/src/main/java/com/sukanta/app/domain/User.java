package com.sukanta.app.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull @Size(max = 50)
	private String firstName;
	
	@NotNull @Size(max = 50)
	private String lastName;
	
	@NotNull @Size(max = 128)
	private String email;
	
	@NotNull @Size(max = 60)
	@JsonIgnore
	private String password;
	
	@Transient
	@JsonIgnore
	private String confirmPassword;
	
}
