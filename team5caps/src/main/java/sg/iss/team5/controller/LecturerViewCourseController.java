package sg.iss.team5.controller;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import sg.iss.team5.caps.SecurityConfigurations;
import sg.iss.team5.model.Module;
import sg.iss.team5.model.Request;
import sg.iss.team5.model.Student;
import sg.iss.team5.model.Studentcourse;
import sg.iss.team5.model.StudentcoursePK;
import sg.iss.team5.model.User;
import sg.iss.team5.service.LecturerService;
import sg.iss.team5.service.StudentService;

//View courses taught & show rating
@Controller
@RequestMapping(value = "/lecturer")
public class LecturerViewCourseController {

	@Autowired
	LecturerService lectservice;

	@Autowired
	StudentService studentservice;

	@RequestMapping(value = "/courselist", method = RequestMethod.GET)
	public ModelAndView listAllbyLectId(HttpSession session) {
		// Security
		if (!SecurityConfigurations.CheckLectAuth(session))
			return new ModelAndView("redirect:/home/login");
		// Security
		String lid = ((UserSession) session.getAttribute("USERSESSION")).getUser().getUserID();
		ArrayList<Module> mlist = lectservice.findPastModuleByLectId(lid);
		ModelAndView mav = new ModelAndView("ViewModules");
		mav.addObject("modules", mlist);

		ArrayList<Double> ratingList = new ArrayList<>();
		for (int i = 0; i < mlist.size(); i++) {
			Double ratingAverage = lectservice.findLecturerRatingByModuleId(mlist.get(i).getModuleID());
			ratingList.add(ratingAverage);
		}
		mav.addObject("ratings", ratingList);

		ArrayList<String> attList = new ArrayList<>();
		for (int i = 0; i < mlist.size(); i++) {
			String attAverage = lectservice.findAttendanceByModuleId(mlist.get(i).getModuleID());
			attList.add(attAverage);
		}
		mav.addObject("attendance", attList);
		return mav;
	}

	/*
	 * @RequestMapping(value = "/list", method = RequestMethod.GET) public
	 * ModelAndView listAll() { ArrayList<Module> mlist =
	 * lectservice.findModuleByLecturerId("L00002"); ModelAndView mav = new
	 * ModelAndView("ViewModules"); // ArrayList<Module> mlist =
	 * (ArrayList<Module>)lectservice.findAll(); mav.addObject("modules", mlist);
	 * return mav; }
	 */

	@RequestMapping(value = "/enrollist/{mid}", method = RequestMethod.GET)
	public ModelAndView findCourseByModuleId(@PathVariable String mid, HttpSession session) {
		// Security
		if (!SecurityConfigurations.CheckLectAuth(session))
			return new ModelAndView("redirect:/home/login");
		// Security
		ArrayList<Studentcourse> mlist = lectservice.findCourseByModuleId(mid);
		ModelAndView mav = new ModelAndView("ViewEnrolment");
		mav.addObject("modules", mlist);
		return mav;
	}

	/*
	 * @RequestMapping(value = "/enrollist/{lid}", method = RequestMethod.GET)
	 * public ModelAndView findModulesByLecturerId(@PathVariable String lid) {
	 * ArrayList<Studentcourse> mlist = lectservice.findModulesByLecturerId(lid);
	 * ModelAndView mav = new ModelAndView("ViewEnrolment");
	 * mav.addObject("modules", mlist); return mav; }
	 */

	/*
	 * @RequestMapping(value = "/enrollist", method = RequestMethod.GET) public
	 * ModelAndView findModuleByLectId(HttpSession session) { String lid =
	 * ((UserSession)session.getAttribute("USERSESSION")).getUser().getUserID();
	 * ArrayList<Module> mlist = lectservice.findCurentModuleByLectId(lid);
	 * ModelAndView mav = new ModelAndView("ViewEnrolment");
	 * mav.addObject("modules", mlist);
	 * 
	 * 
	 * ArrayList<Studentcourse> scourse = new ArrayList<>(); for (Module current:
	 * mlist) { for(Studentcourse c1:
	 * lectservice.findCourseByModuleId(current.getModuleID())) { scourse.add(c1); }
	 * 
	 * } mav.addObject("courses", scourse);
	 * 
	 * return mav; }
	 */

	@RequestMapping(value = { "/request" }, method = RequestMethod.GET)
	public ModelAndView showRequest(Model model, HttpSession session) {
		// Security
		if (!SecurityConfigurations.CheckLectAuth(session))
			return new ModelAndView("redirect:/home/login");
		// Security
		String lid = ((UserSession) session.getAttribute("USERSESSION")).getUser().getUserID();
		model.addAttribute("request", new Request());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("requestEnrollment");
		ArrayList<String> newList = new ArrayList<String>();
		ArrayList<String> mList = lectservice.getAllModuleIDForCurrentYear();
		for (String moduleid : mList) {
			if (moduleid.startsWith(lid.substring(4, 6))) {
				newList.add(moduleid);
			}
		}
		mav.addObject("modules", newList);
		return mav;
	}

	@RequestMapping(value = { "/request" }, method = RequestMethod.POST)
	public ModelAndView showRequest(@ModelAttribute("request") Request request, HttpSession session) {
		// Security
		if (!SecurityConfigurations.CheckLectAuth(session))
			return new ModelAndView("redirect:/home/login");
		// Security
		ModelAndView mav = new ModelAndView();
		Student student = lectservice.findStudentByStudentID(request.getStudentID());
		if (student == null)
			return new ModelAndView("requestFail");
		Studentcourse sc = lectservice.findStudentcourseByPK(request.getStudentID(), request.getModuleID());
		if (sc != null) {
			mav.setViewName("requestFail");
			return mav;
		}
		Module module = lectservice.findModuleByModuleID(request.getModuleID());
		System.out.println(module.getCoursedetail().getCourseName());
		request.setCourseName(module.getCoursedetail().getCourseName());
		request.setLecturerName(
				module.getLecturer().getUser().getFirstName() + " " + module.getLecturer().getUser().getLastName());
		request.setModuleID(module.getModuleID());
		request.setVenue(module.getVenue());
		User user = lectservice.findUserByStudentID(request.getStudentID());
		request.setStudentName(user.getFirstName() + " " + user.getLastName());
		mav.addObject("newRequest", request);
		mav.setViewName("confirmRequest");
		session.setAttribute("REQUEST", request);
		return mav;
	}

	@RequestMapping(value = { "/confirm/{mid}" }, method = RequestMethod.POST)
	public String showConfirmRequest(@ModelAttribute("request") Request request, HttpSession session,
			@PathVariable String mid) {
		// Security
		if (!SecurityConfigurations.CheckLectAuth(session))
			return "redirect:/home/login";
		// Security
		Studentcourse sc = new Studentcourse();
		StudentcoursePK scPK = new StudentcoursePK();
		Module module = new Module();
		module = lectservice.findModuleByModuleID(mid);
		request = (Request) session.getAttribute("REQUEST");
		scPK.setModuleID(mid);
		Student student = lectservice.findStudentByStudentID(request.getStudentID());
		sc.setStudent(student);
		scPK.setStudentID(student.getStudentID());
		sc.setId(scPK);
		sc.setModule(module);
		sc.setEnrollStatus("Pending");
		sc.setEnrollTime(Calendar.getInstance().getTime());
		// module.getStudentcourses().add(sc);
		// lecturerService.updateModule(module);
		lectservice.createStudentcourse(sc);
		return "requestSuccess";
	}
}