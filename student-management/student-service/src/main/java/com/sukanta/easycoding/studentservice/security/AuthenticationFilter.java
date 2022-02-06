package com.sukanta.easycoding.studentservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sukanta.easycoding.studentservice.model.LoginRequest;
import com.sukanta.easycoding.studentservice.model.Student;
import com.sukanta.easycoding.studentservice.service.StudentService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	StudentService studentService;
	Environment environment;

	@Autowired
	public AuthenticationFilter(StudentService studentService, AuthenticationManager authenticationManager,
			Environment environment) {
		super.setAuthenticationManager(authenticationManager);
		this.studentService = studentService;
		this.environment = environment;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			log.info("From Student-Service -> Server name : {} ", request.getServerName());
			LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
					loginRequest.getEmail(), loginRequest.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String studentEmail = ((User) authResult.getPrincipal()).getUsername();
		Student student = studentService.getStudentByEmailId(studentEmail);
		String jwtToken = Jwts.builder().setSubject(student.getId().toString())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expirationTime"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret")).compact();
		response.addHeader("token", jwtToken);
		response.addHeader("studentId", student.getId().toString());
	}

}
