/**
 * 
 */
package com.covid19.vaccine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.covid19.vaccine.entity.EmailFrequency;

/**
 * @author Sukanta Biswas
 *
 */
public interface EmailFrequencyRepository extends JpaRepository<EmailFrequency, Long> {
	EmailFrequency findByHour(int hour);
}
