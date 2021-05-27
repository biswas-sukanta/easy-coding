/**
 * 
 */
package com.covid19.vaccine.service;

import com.covid19.vaccine.model.VaccineRequest;

/**
 * @author Sukanta Biswas
 *
 */
public interface VaccineChecker {

	void checkAvailability(VaccineRequest vaccineRequest);
}
