package com.sukanta.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sukanta.app.aspect.RequiresCaptcha;
import com.sukanta.app.domain.User;
import com.sukanta.app.repositoty.UserRepository;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	UserRepository userRepository;

	@RequiresCaptcha
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid User user) {
		userRepository.save(user);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> users() {
		return ResponseEntity.ok(userRepository.findAll());
	}
}
