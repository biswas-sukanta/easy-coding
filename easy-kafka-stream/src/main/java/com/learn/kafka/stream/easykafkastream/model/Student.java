package com.learn.kafka.stream.easykafkastream.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ID", "Name", "Gender", "Class", "Seat", "Club", "Persona", "Crush", "BreastSize", "Strength",
		"Hairstyle", "Color", "Eyes", "EyeType", "Stockings", "Accessory", "ScheduleTime", "ScheduleDestination",
		"ScheduleAction", "Info" })
public class Student {

	@JsonProperty("ID")
	private String iD;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Gender")
	private String gender;
	@JsonProperty("Class")
	private String _class;
	@JsonProperty("Seat")
	private String seat;
	@JsonProperty("Club")
	private String club;
	@JsonProperty("Persona")
	private String persona;
	@JsonProperty("Crush")
	private String crush;
	@Override
	public String toString() {
		return "Student [iD=" + iD + ", name=" + name + ", gender=" + gender + ", _class=" + _class + ", seat=" + seat
				+ ", club=" + club + ", persona=" + persona + ", crush=" + crush + ", breastSize=" + breastSize
				+ ", strength=" + strength + ", hairstyle=" + hairstyle + ", color=" + color + ", eyes=" + eyes
				+ ", eyeType=" + eyeType + ", stockings=" + stockings + ", accessory=" + accessory + ", scheduleTime="
				+ scheduleTime + ", scheduleDestination=" + scheduleDestination + ", scheduleAction=" + scheduleAction
				+ ", info=" + info + "]";
	}

	@JsonProperty("BreastSize")
	private String breastSize;
	@JsonProperty("Strength")
	private String strength;
	@JsonProperty("Hairstyle")
	private String hairstyle;
	@JsonProperty("Color")
	private String color;
	@JsonProperty("Eyes")
	private String eyes;
	@JsonProperty("EyeType")
	private String eyeType;
	@JsonProperty("Stockings")
	private String stockings;
	@JsonProperty("Accessory")
	private String accessory;
	@JsonProperty("ScheduleTime")
	private String scheduleTime;
	@JsonProperty("ScheduleDestination")
	private String scheduleDestination;
	@JsonProperty("ScheduleAction")
	private String scheduleAction;
	@JsonProperty("Info")
	private String info;
	@JsonProperty("ID")
	public String getID() {
		return iD;
	}

	@JsonProperty("ID")
	public void setID(String iD) {
		this.iD = iD;
	}

	public Student withID(String iD) {
		this.iD = iD;
		return this;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	public Student withName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty("Gender")
	public String getGender() {
		return gender;
	}

	@JsonProperty("Gender")
	public void setGender(String gender) {
		this.gender = gender;
	}

	public Student withGender(String gender) {
		this.gender = gender;
		return this;
	}

	@JsonProperty("Class")
	public String getClass_() {
		return _class;
	}

	@JsonProperty("Class")
	public void setClass_(String _class) {
		this._class = _class;
	}

	public Student withClass(String _class) {
		this._class = _class;
		return this;
	}

	@JsonProperty("Seat")
	public String getSeat() {
		return seat;
	}

	@JsonProperty("Seat")
	public void setSeat(String seat) {
		this.seat = seat;
	}

	public Student withSeat(String seat) {
		this.seat = seat;
		return this;
	}

	@JsonProperty("Club")
	public String getClub() {
		return club;
	}

	@JsonProperty("Club")
	public void setClub(String club) {
		this.club = club;
	}

	public Student withClub(String club) {
		this.club = club;
		return this;
	}

	@JsonProperty("Persona")
	public String getPersona() {
		return persona;
	}

	@JsonProperty("Persona")
	public void setPersona(String persona) {
		this.persona = persona;
	}

	public Student withPersona(String persona) {
		this.persona = persona;
		return this;
	}

	@JsonProperty("Crush")
	public String getCrush() {
		return crush;
	}

	@JsonProperty("Crush")
	public void setCrush(String crush) {
		this.crush = crush;
	}

	public Student withCrush(String crush) {
		this.crush = crush;
		return this;
	}

	@JsonProperty("BreastSize")
	public String getBreastSize() {
		return breastSize;
	}

	@JsonProperty("BreastSize")
	public void setBreastSize(String breastSize) {
		this.breastSize = breastSize;
	}

	public Student withBreastSize(String breastSize) {
		this.breastSize = breastSize;
		return this;
	}

	@JsonProperty("Strength")
	public String getStrength() {
		return strength;
	}

	@JsonProperty("Strength")
	public void setStrength(String strength) {
		this.strength = strength;
	}

	public Student withStrength(String strength) {
		this.strength = strength;
		return this;
	}

	@JsonProperty("Hairstyle")
	public String getHairstyle() {
		return hairstyle;
	}

	@JsonProperty("Hairstyle")
	public void setHairstyle(String hairstyle) {
		this.hairstyle = hairstyle;
	}

	public Student withHairstyle(String hairstyle) {
		this.hairstyle = hairstyle;
		return this;
	}

	@JsonProperty("Color")
	public String getColor() {
		return color;
	}

	@JsonProperty("Color")
	public void setColor(String color) {
		this.color = color;
	}

	public Student withColor(String color) {
		this.color = color;
		return this;
	}

	@JsonProperty("Eyes")
	public String getEyes() {
		return eyes;
	}

	@JsonProperty("Eyes")
	public void setEyes(String eyes) {
		this.eyes = eyes;
	}

	public Student withEyes(String eyes) {
		this.eyes = eyes;
		return this;
	}

	@JsonProperty("EyeType")
	public String getEyeType() {
		return eyeType;
	}

	@JsonProperty("EyeType")
	public void setEyeType(String eyeType) {
		this.eyeType = eyeType;
	}

	public Student withEyeType(String eyeType) {
		this.eyeType = eyeType;
		return this;
	}

	@JsonProperty("Stockings")
	public String getStockings() {
		return stockings;
	}

	@JsonProperty("Stockings")
	public void setStockings(String stockings) {
		this.stockings = stockings;
	}

	public Student withStockings(String stockings) {
		this.stockings = stockings;
		return this;
	}

	@JsonProperty("Accessory")
	public String getAccessory() {
		return accessory;
	}

	@JsonProperty("Accessory")
	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public Student withAccessory(String accessory) {
		this.accessory = accessory;
		return this;
	}

	@JsonProperty("ScheduleTime")
	public String getScheduleTime() {
		return scheduleTime;
	}

	@JsonProperty("ScheduleTime")
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Student withScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
		return this;
	}

	@JsonProperty("ScheduleDestination")
	public String getScheduleDestination() {
		return scheduleDestination;
	}

	@JsonProperty("ScheduleDestination")
	public void setScheduleDestination(String scheduleDestination) {
		this.scheduleDestination = scheduleDestination;
	}

	public Student withScheduleDestination(String scheduleDestination) {
		this.scheduleDestination = scheduleDestination;
		return this;
	}

	@JsonProperty("ScheduleAction")
	public String getScheduleAction() {
		return scheduleAction;
	}

	@JsonProperty("ScheduleAction")
	public void setScheduleAction(String scheduleAction) {
		this.scheduleAction = scheduleAction;
	}

	public Student withScheduleAction(String scheduleAction) {
		this.scheduleAction = scheduleAction;
		return this;
	}

	@JsonProperty("Info")
	public String getInfo() {
		return info;
	}

	@JsonProperty("Info")
	public void setInfo(String info) {
		this.info = info;
	}

	public Student withInfo(String info) {
		this.info = info;
		return this;
	}
}