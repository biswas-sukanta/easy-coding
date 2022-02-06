package com.sukanta.easycoding.studentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.sukanta.easycoding.studentservice.entity.StudentEntity;
import com.sukanta.easycoding.studentservice.model.Student;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
	StudentEntity studentToEntity(Student student);
	Student entityToStudent(StudentEntity studentEntity);
}