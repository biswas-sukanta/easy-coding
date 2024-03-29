/**
 * 
 */
package com.covid19.vaccine.service;

import java.io.StringWriter;
import java.util.List;

import com.covid19.vaccine.entity.VaccineEntity;
import com.covid19.vaccine.model.EmailData;

/**
 * @author Sukanta Biswas
 *
 */
public interface VaccineChecker<T> {

	void checkAvailability(T vaccineRequest, String day);

	VaccineEntity registerUser(T record);

	List<T> getAllVaccineRecords();

	StringWriter settingEmailTemplate(final List<EmailData> emailDatas);

	void checkMultiAvailability(T vaccineRequest, String day);
}
