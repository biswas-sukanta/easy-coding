/**
 * 
 */
package easy.spring.EasySpringJwtApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import easy.spring.EasySpringJwtApp.dto.AuthDto;
import easy.spring.EasySpringJwtApp.dto.StudentDto;
import easy.spring.EasySpringJwtApp.service.StudentAuthService;
import easy.spring.EasySpringJwtApp.service.StudentService;
import easy.spring.EasySpringJwtApp.util.JwtUtil;

/**
 * @author Sukanta Biswas
 *
 */

@RestController
public class StudentController {

	@Autowired
	private StudentAuthService stuAuthService;
	
	@Autowired
	private StudentService stuService;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtil jUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<String> authenticate(@RequestBody AuthDto authDto) {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

		UserDetails studentDetails = stuAuthService.loadUserByUsername(authDto.getUsername());
		String token = jUtil.generateToken(studentDetails);
		return ResponseEntity.ok(token);
	}

	@GetMapping("/student/{id}")
	public ResponseEntity<StudentDto> getStudent(@PathVariable long id) {

		// Todo

		return ResponseEntity.ok(stuService.getStudent(id));
	}
}
