/**
 * 
 */
package easy.spring.EasySpringJwtApp.dto;

import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */

@Data
public class StudentDto {

	public long id;

	public String stuId;

	public String stuName;

	public String stuDept;
	
	public String username;
	
	public String password;
}
