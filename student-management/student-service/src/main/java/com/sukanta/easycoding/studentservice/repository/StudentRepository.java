package com.sukanta.easycoding.studentservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sukanta.easycoding.studentservice.entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
	Optional<StudentEntity> findByFirstName(String studentFirstName);

	Optional<StudentEntity> findByEmail(String email);
}
