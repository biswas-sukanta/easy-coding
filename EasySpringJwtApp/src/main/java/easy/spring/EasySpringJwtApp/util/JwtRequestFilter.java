/**
 * 
 */
package easy.spring.EasySpringJwtApp.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import easy.spring.EasySpringJwtApp.service.StudentAuthService;

/**
 * @author Sukanta Biswas
 *
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jUtil;
	
	@Autowired
	private StudentAuthService stuAuthService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		String authHeader = req.getHeader("Authorization");
		
		String studentName = null;
		String token = null;
		if(null != authHeader && authHeader.startsWith("Bearer")) {
			 token = authHeader.substring(7);
			studentName = jUtil.extractUsername(token);
		}
		
		if(null!=studentName) {
			UserDetails studentDetails = stuAuthService.loadUserByUsername(studentName);
			
			if(jUtil.validateToken(token, studentDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentDetails, "", studentDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		chain.doFilter(req, res);
	}
	
}
