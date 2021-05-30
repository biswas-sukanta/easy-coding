/**
 * 
 */
package com.covid19.vaccine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.covid19.vaccine.entity.VaccineEntity;

/**
 * @author Sukanta Biswas
 *
 */
public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {
	List<VaccineEntity> findByEmailIdAndMobileNoAndPincode(String emailId, String mobileNo, String pincode);
}
