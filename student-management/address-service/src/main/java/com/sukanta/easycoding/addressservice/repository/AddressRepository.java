package com.sukanta.easycoding.addressservice.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sukanta.easycoding.addressservice.entity.AddressEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
	Optional<Set<AddressEntity>> findByStudentEmail(String studentEmail);
}
