/**
 * 
 */
package easy.spring.EasySpringJwtApp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Sukanta Biswas
 *
 */

@Data
@Entity
@Table(name = "Student")
public class StudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column(name = "student_id")
	public String stuId;

	@Column(name = "student_name")
	public String stuName;

	@Column(name = "student_dept")
	public String stuDept;
	
	@Column(name = "username")
	public String username;

	@Column(name = "password")
	public String password;

}
