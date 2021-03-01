/**
 * 
 */
package easy.spring.EasySpringJwtApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */

@Data
@AllArgsConstructor
public class AuthDto {

	public String jwtToken;

	public String username;

	public char[] password;

}
