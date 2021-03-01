/**
 * 
 */
package easy.spring.EasySpringJwtApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easy.spring.EasySpringJwtApp.entity.StudentEntity;

/**
 * @author Sukanta Biswas
 *
 */
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

	public StudentEntity getStudentByUsername(String username);

}
