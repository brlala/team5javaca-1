package sg.iss.team5.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the students database table.
 * 
 */
@Entity
@Table(name="students")
@NamedQuery(name="Student.findAll", query="SELECT s FROM Student s")
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String studentID;

	@Temporal(TemporalType.DATE)
	private Date enrollmentDate;

	private String status;

	//bi-directional many-to-one association to Studentcourse
	@OneToMany(mappedBy="student")
	private List<Studentcourse> studentcourses;

	//bi-directional one-to-one association to User
	@OneToOne
	@JoinColumn(name="StudentID")
	private User user;

	public Student() {
	}

	public String getStudentID() {
		return this.studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public Date getEnrollmentDate() {
		return this.enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Studentcourse> getStudentcourses() {
		return this.studentcourses;
	}

/*	public void setStudentcourses(List<Studentcourse> studentcourses) {
		this.studentcourses = studentcourses;
	}*/

	public Studentcourse addStudentcours(Studentcourse studentcours) {
		getStudentcourses().add(studentcours);
		studentcours.setStudent(this);

		return studentcours;
	}

	public Studentcourse removeStudentcours(Studentcourse studentcours) {
		getStudentcourses().remove(studentcours);
		studentcours.setStudent(null);

		return studentcours;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}