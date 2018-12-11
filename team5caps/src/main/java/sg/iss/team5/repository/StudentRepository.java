package sg.iss.team5.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.iss.team5.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
	
	@Query(value = "select * from students", nativeQuery = true)
	ArrayList<Student> findAllStudents();
	
	@Query(value="select * from students s where s.studentid =: sid", nativeQuery = true)
	Student findStudentById(@Param("sid") String sid);
	
//
//	if custom methods are needed, add the interface method down below and 
	//use another implementation with the following convention
	
//	1. public class StudentRepositoryImplementation implements StudentRepository {
//	2. add the interface to this class, this will allow polymorphism
//	source: https://dzone.com/articles/add-custom-functionality-to-a-spring-data-reposito
//
//	List<Course> findByNameOrderByIdDesc(String name);
//
//	List<Course> deleteByName(String name);
//
//	@Query("Select  c  From Course c where name like '%100 Steps'")
//	List<Course> courseWith100StepsInName();
//
//	@Query(value = "Select  *  From Course c where name like '%100 Steps'", nativeQuery = true)
//	List<Course> courseWith100StepsInNameUsingNativeQuery();
//
//	@Query(name = "query_get_100_Step_courses")
//	List<Course> courseWith100StepsInNameUsingNamedQuery();
}
