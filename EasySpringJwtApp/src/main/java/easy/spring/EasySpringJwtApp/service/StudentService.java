/**
 * 
 */
package easy.spring.EasySpringJwtApp.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easy.spring.EasySpringJwtApp.dto.StudentDto;
import easy.spring.EasySpringJwtApp.entity.StudentEntity;
import easy.spring.EasySpringJwtApp.repository.StudentRepository;

/**
 * @author Sukanta Biswas
 *
 */

@Service
public class StudentService {

	@Autowired
	private StudentRepository repository;

	@Autowired
	private ModelMapper mapper;

	public StudentDto getStudent(long id) {
		Optional<StudentEntity> studentEntity = repository.findById(id);
		return studentEntity.isPresent() ? mapper.map(studentEntity.get(), StudentDto.class) : new StudentDto();
	}

	public StudentDto getStudentByUsername(String username) {
		return mapper.map(repository.getStudentByUsername(username), StudentDto.class);
	}
}
