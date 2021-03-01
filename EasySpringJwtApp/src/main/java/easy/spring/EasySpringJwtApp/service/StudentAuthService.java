/**
 * 
 */
package easy.spring.EasySpringJwtApp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import easy.spring.EasySpringJwtApp.dto.StudentDto;

/**
 * @author Sukanta Biswas
 *
 */

@Service
public class StudentAuthService implements UserDetailsService {

	@Autowired
	private StudentService studentService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		StudentDto student = studentService.getStudentByUsername(username);
		return new User(student.getUsername(), student.getPassword(), new ArrayList<>());
	}
}
