package com.sukanta.easycoding.studentservice.security;

import com.sukanta.easycoding.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	StudentService studentService;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	Environment environment;

	@Autowired
	public WebSecurity(StudentService studentService, Environment environment,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.studentService = studentService;
		this.environment = environment;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().disable();

		http.authorizeRequests()
				.antMatchers(HttpMethod.GET,
						environment.getProperty("student.register.url")).permitAll()
				.antMatchers(HttpMethod.POST,
						environment.getProperty("login.url.path")).permitAll().and()
				.authorizeRequests().and().addFilter(getAuthenticationFilter());

/*		http.authorizeRequests()
				.antMatchers(HttpMethod.GET,
						environment.getProperty("student.register.url")).permitAll()
				.antMatchers(HttpMethod.POST,
						environment.getProperty("login.url.path")).permitAll().and()
				.authorizeRequests().antMatchers("/**")
				.hasIpAddress(environment.getProperty("gateway.ip"))
//				 hasIpAddress() or access()
//				.access("@gatewayAddressChecker.isLocalHost(authentication,request)")
				.and().addFilter(getAuthenticationFilter());*/
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(studentService, authenticationManager(),
				environment);
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(studentService).passwordEncoder(bCryptPasswordEncoder);
	}
}