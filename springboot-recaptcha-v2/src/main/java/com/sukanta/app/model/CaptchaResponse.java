package com.sukanta.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class CaptchaResponse {

    private Boolean success;
    private Date challenge_ts;
    private String hostname;
    @JsonProperty("error-codes")
    private List<String> errorCodes;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Date getTimestamp() {
        return challenge_ts;
    }

    public void setTimestamp(Date challenge_ts) {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

	public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

	@Override
	public String toString() {
		return "CaptchaResponse [success=" + success + ", challenge_ts=" + challenge_ts + ", hostname=" + hostname
				+ ", errorCodes=" + errorCodes + "]";
	}
}
