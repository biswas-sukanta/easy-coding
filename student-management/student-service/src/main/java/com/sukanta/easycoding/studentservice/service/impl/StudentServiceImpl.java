package com.sukanta.easycoding.studentservice.service.impl;

import com.sukanta.easycoding.studentservice.entity.StudentEntity;
import com.sukanta.easycoding.studentservice.exception.ItemNotFoundException;
import com.sukanta.easycoding.studentservice.feignclients.AddressFeignClient;
import com.sukanta.easycoding.studentservice.mapper.StudentMapper;
import com.sukanta.easycoding.studentservice.model.Address;
import com.sukanta.easycoding.studentservice.model.Student;
import com.sukanta.easycoding.studentservice.repository.StudentRepository;
import com.sukanta.easycoding.studentservice.service.StudentFallbackService;
import com.sukanta.easycoding.studentservice.service.StudentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Supplier;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService, StudentFallbackService {

	int serviceCallCount = 0;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	AddressFeignClient addressServiceFeign;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

	@Override
	public Student getStudentByName(String studentFirstName) {
		Student student = studentMapper.entityToStudent(studentRepository.findByFirstName(studentFirstName)
				.orElseThrow(() -> new ItemNotFoundException("Item not found : " + studentFirstName)));
		student.setAddress(Collections.unmodifiableSet(Objects.requireNonNull(findAddressByEmail(student.getEmail()).getBody())));
		return student;
	}
	
	@Override
	public Student getStudentByEmailId(String studentEmailId) {
		Student student = studentMapper.entityToStudent(studentRepository.findByEmail(studentEmailId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found : " + studentEmailId)));
		student.setAddress(Collections.unmodifiableSet(Objects.requireNonNull(findAddressByEmail(student.getEmail()).getBody())));
		return student;
	}

	@Override
	@Transactional
	public Student addStudent(@RequestBody Student student) {
		student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
		Optional<Address> firstStudent = student.getAddress().stream().findFirst();
		if (firstStudent.isPresent()) {
			ResponseEntity<Address> studentAddress = addressServiceFeign.addAddress(firstStudent.get());
			log.info("Address : {}", studentAddress);
		}
		return studentMapper.entityToStudent(studentRepository.saveAndFlush(studentMapper.studentToEntity(student)));
	}

	@Override
	public ResponseEntity<Address> addAddress(Address address) {
		log.info("inside StudentServiceImpl-addAddres()");
		return addressServiceFeign.addAddress(address);
	}

	@Override
	@CircuitBreaker(name = "addressService", fallbackMethod = "findAddressByEmailfallback")
	public ResponseEntity<Set<Address>> findAddressByEmail(String studentEmail) {
		log.info("inside StudentServiceImpl-findAddressByEmail() call count : {}", serviceCallCount++);
		return addressServiceFeign.findAddressByEmail(studentEmail);
	}

	@Override
	public ResponseEntity<Set<Address>> findAddressByEmailfallback(String studentEmail, Throwable throwable) {
		log.error("inside StudentServiceImpl-findAddressByEmailfallback() exception : {}", throwable.getMessage());
		return new ResponseEntity<>(
				Set.of(Address.builder().city("No City").pincode("No Pincode").studentEmail("No Email").build()),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@Override
	public void addSampleData() {
		RetryConfig config = RetryConfig.<ResponseEntity<Address>>custom().maxAttempts(5)
				.retryOnResult(
						response -> response == null || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
				.retryExceptions(Exception.class)
//				.waitDuration(Duration.ofSeconds(5))
				.intervalFunction(IntervalFunction.ofExponentialBackoff(5000, 3)).build();
		RetryRegistry registry = RetryRegistry.of(config);
		Retry retry = registry.retry("studentService", config);
		Address address = Address.builder().id(UUID.randomUUID()).city("Kolkata")
				.studentEmail("biswas.sukanta@hotmail.com").pincode("741201").build();
		Supplier<ResponseEntity<Address>> retryingAddAddress = Retry.decorateSupplier(retry, () -> addAddress(address));

		log.info("Posted Address : {} ", retryingAddAddress.get());

		StudentEntity studentEntity = new StudentEntity();
		studentEntity.setId(UUID.randomUUID());
		studentEntity.setFirstName("Sukanta");
		studentEntity.setLastName("Biswas");
		studentEntity.setEmail(address.getStudentEmail());
		studentEntity.setPassword(bCryptPasswordEncoder.encode("testPass"));
		log.info("Posted Student : {} ", studentRepository.saveAndFlush(studentEntity));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<StudentEntity> stOptional = studentRepository.findByEmail(username);
		if (stOptional.isEmpty()) {
			throw new UsernameNotFoundException("Invalid Credentials");
		}
		return new User(stOptional.get().getEmail(), stOptional.get().getPassword(), new ArrayList<>());
	}
}