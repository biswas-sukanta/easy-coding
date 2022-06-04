/**
 * 
 */
package com.covid19.vaccine.service;

import java.util.List;

import com.covid19.vaccine.entity.VaccineEntity;

/**
 * @author Sukanta Biswas
 *
 */
public interface VaccineChecker<T> {

	void checkAvailability(T vaccineRequestm, String day);

	VaccineEntity registerUser(T record);

	List<T> getAllVaccineRecords();
}
