package com.learn.kafka.stream.easykafkastream.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "userId", "jobTitleName", "firstName", "lastName", "preferredFullName", "employeeCode", "region",
		"phoneNumber", "emailAddress" })
public class Employee {

	@JsonProperty("userId")
	private String userId;
	@JsonProperty("jobTitleName")
	private String jobTitleName;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("preferredFullName")
	private String preferredFullName;
	@JsonProperty("employeeCode")
	private String employeeCode;
	@JsonProperty("region")
	private String region;
	@JsonProperty("phoneNumber")
	private String phoneNumber;
	@JsonProperty("emailAddress")
	private String emailAddress;

	@JsonProperty("userId")
	public String getUserId() {
		return userId;
	}

	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("jobTitleName")
	public String getJobTitleName() {
		return jobTitleName;
	}

	@JsonProperty("jobTitleName")
	public void setJobTitleName(String jobTitleName) {
		this.jobTitleName = jobTitleName;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("preferredFullName")
	public String getPreferredFullName() {
		return preferredFullName;
	}

	@JsonProperty("preferredFullName")
	public void setPreferredFullName(String preferredFullName) {
		this.preferredFullName = preferredFullName;
	}

	@JsonProperty("employeeCode")
	public String getEmployeeCode() {
		return employeeCode;
	}

	@JsonProperty("employeeCode")
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}

	@JsonProperty("phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty("phoneNumber")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@JsonProperty("emailAddress")
	public String getEmailAddress() {
		return emailAddress;
	}

	@Override
	public String toString() {
		return "Employee [userId=" + userId + ", jobTitleName=" + jobTitleName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", preferredFullName=" + preferredFullName + ", employeeCode="
				+ employeeCode + ", region=" + region + ", phoneNumber=" + phoneNumber + ", emailAddress="
				+ emailAddress + "]";
	}

	@JsonProperty("emailAddress")
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}