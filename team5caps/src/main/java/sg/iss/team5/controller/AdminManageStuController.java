package sg.iss.team5.controller;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import antlr.StringUtils;
import sg.iss.team5.model.Student;
import sg.iss.team5.model.User;
import sg.iss.team5.service.AdminService;

@Controller
@RequestMapping(value = "/admin")
public class AdminManageStuController {

	@Autowired
	private AdminService adminService;

	@RequestMapping(value = { "/manage/student" }, method = RequestMethod.GET)
	public ModelAndView showTesting() {
		ArrayList<Student> myStudentList = new ArrayList<Student>();
		myStudentList = adminService.findAllStudents();
		for (Student aStudent : myStudentList) {
			System.out.println(aStudent);
		}
		System.out.println("HAHAHAHAHAHAH");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ListStudent");
		mv.addObject("sList", myStudentList);
		return mv;
	}

	@RequestMapping(value = { "/student/create" }, method = RequestMethod.GET)
	public ModelAndView newCoursepage() {
		String sid = adminService.findAllStudents().get(adminService.findAllStudents().size() - 1).getStudentID();
		int num = Integer.parseInt(sid.substring(1, 6));
		String id = "S" + String.format("%05d", num + 1);
		ModelAndView mav = new ModelAndView("FormStudent", "student", new Student());
		mav.addObject("SID", id);
		return mav;
	}

	@RequestMapping(value = { "/student/create" }, method = RequestMethod.POST)
	public ModelAndView createNewLecturer(@ModelAttribute Student student, BindingResult result) {
		if (result.hasErrors())
			return new ModelAndView("FormStudent");
		student.setEnrollmentDate(Calendar.getInstance().getTime());
		student.getUser().setUserID(student.getStudentID());
		student.getUser().setPassword("Password");
		student.getUser().setAccessLevel("Student");
		adminService.createStudent(student, student.getUser());

		ModelAndView mav = new ModelAndView("redirect:/admin/manage/student");
		return mav;
	}

	@RequestMapping(value = "/student/edit/{studentID}", method = RequestMethod.GET)
	public ModelAndView editStudentPage(@PathVariable String studentID) {
		ModelAndView mav = new ModelAndView("FormStudentEdit");
		mav.addObject("student", adminService.findStudent(studentID));
		return mav;
	}

	@RequestMapping(value = "/student/edit/{studentID}", method = RequestMethod.POST)
	public ModelAndView editStudent(@ModelAttribute @Valid Student student, BindingResult result,
			@PathVariable String studentID, final RedirectAttributes redirectAttributes) {
		System.out.println("student" + student.toString());
		if (result.hasErrors())
			return new ModelAndView("FormStudentEdit");
		User user = student.getUser();
		user.setUserID(student.getStudentID());
		user.setAccessLevel(adminService.findStudentById(student.getStudentID()).getUser().getAccessLevel());
		user.setPassword(adminService.findStudentById(student.getStudentID()).getUser().getPassword());
		student.setEnrollmentDate(adminService.findStudentById(student.getStudentID()).getEnrollmentDate());
		// System.out.println(student.get);
		adminService.createStudent(student, user);
		ModelAndView mav = new ModelAndView("redirect:/admin/manage/student");
		String message = "Student" + student.getStudentID() + " was successfully updated.";
		redirectAttributes.addFlashAttribute("message", message);
		return mav;
	}
}