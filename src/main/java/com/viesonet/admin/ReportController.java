package com.viesonet.admin;

import java.util.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viesonet.security.AuthConfig;
import com.viesonet.entity.*;
import com.viesonet.service.PostsService;
import com.viesonet.service.UsersService;
import com.viesonet.service.sp_FilterPostLikeService;
import com.viesonet.service.sp_Last7DaySumAccountsService;
import com.viesonet.service.sp_ListAccService;
import com.viesonet.service.sp_ListYearService;
import com.viesonet.service.sp_NumberReportService;
import com.viesonet.service.sp_SumAccountsByDayService;
import com.viesonet.service.sp_TopPostLikeService;
import com.viesonet.service.sp_TotalPostsService;
import com.viesonet.service.sp_ViolationsPostsService;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@CrossOrigin("*")
public class ReportController {

	@Autowired
	UsersService usersService;

	@Autowired
	PostsService postsService;

	@Autowired
	sp_FilterPostLikeService filterPostsLike;

	@Autowired
	sp_ListYearService EXEC1;

	@Autowired
	sp_TopPostLikeService topLike;

	@Autowired
	sp_ListAccService listCountAcc;

	@Autowired
	sp_ViolationsPostsService EXEC;

	@Autowired
	sp_NumberReportService EXEC2;

	@Autowired
	sp_TotalPostsService totalPosts;

	@Autowired
	sp_Last7DaySumAccountsService last7DaySumAccounts;

	@Autowired
	sp_SumAccountsByDayService sumAccountsByDay;

	// @GetMapping("/admin/report")
	// public ModelAndView getHomePage() {
	// ModelAndView modelAndView = new ModelAndView("/admin/report");
	// return modelAndView;
	// }

	@GetMapping("/admin/reportViolationsPosts")
	public List<ViolationsPosts> violationsPosts() {
		return EXEC.violationsPosts(LocalDate.now().getYear());
	}

	@GetMapping("/admin/reportNumberReport")
	public List<NumberReport> numberReport() {
		return EXEC2.numberReports(LocalDate.now().getYear());
	}

	@GetMapping("/admin/reportListYear")
	public List<ListYear> listYear() {
		return EXEC1.listYears();
	}

	@GetMapping("/admin/reportAge")
	public double Age() {
		// Lấy danh sách các người dùng
		List<Users> usersList = usersService.findAll();

		// Tạo danh sách nhóm tuổi
		List<Users> age18to25 = new ArrayList<>();
		List<Users> from25to35 = new ArrayList<>();
		List<Users> from35andAbove = new ArrayList<>();

		// Chạy vòng lặp để thêm vào từng nhóm
		for (Users user : usersList) {
			int age = getAge(user.getBirthday());
			String category = getCategory(age);

			if (category.equals("Từ 18 đến 25 tuổi")) {
				age18to25.add(user);
			} else if (category.equals("Từ 25 đến 35 tuổi")) {
				from25to35.add(user);
			} else if (category.equals("35 tuổi trở lên")) {
				from35andAbove.add(user);
			}
		}

		// Đếm tổng số lượng tài khoản
		double tongSo = usersList.size();

		// Tính phần trăm độ tuổi
		double nhom18den25 = (age18to25.size() / tongSo) * 100;

		return Math.round(nhom18den25 * 10 + 0.05) / 10.0;
	}

	@GetMapping("/admin/reportAge2")
	public double Age2() {
		// Lấy danh sách các người dùng
		List<Users> usersList = usersService.findAll();

		// Tạo danh sách nhóm tuổi
		List<Users> age18to25 = new ArrayList<>();
		List<Users> from25to35 = new ArrayList<>();
		List<Users> from35andAbove = new ArrayList<>();

		// Chạy vòng lặp để thêm vào từng nhóm
		for (Users user : usersList) {
			int age = getAge(user.getBirthday());
			String category = getCategory(age);

			if (category.equals("Từ 18 đến 25 tuổi")) {
				age18to25.add(user);
			} else if (category.equals("Từ 25 đến 35 tuổi")) {
				from25to35.add(user);
			} else if (category.equals("35 tuổi trở lên")) {
				from35andAbove.add(user);
			}
		}

		// Đếm tổng số lượng tài khoản
		double tongSo = usersList.size();

		// Tính phần trăm độ tuổi
		double nhom25den35 = (from25to35.size() / tongSo) * 100;

		return Math.round(nhom25den35 * 10 + 0.05) / 10.0;
	}

	@GetMapping("/admin/reportAge3")
	public double Age3() {
		// Lấy danh sách các người dùng
		List<Users> usersList = usersService.findAll();

		// Tạo danh sách nhóm tuổi
		List<Users> age18to25 = new ArrayList<>();
		List<Users> from25to35 = new ArrayList<>();
		List<Users> from35andAbove = new ArrayList<>();

		// Chạy vòng lặp để thêm vào từng nhóm
		for (Users user : usersList) {
			int age = getAge(user.getBirthday());
			String category = getCategory(age);

			if (category.equals("Từ 18 đến 25 tuổi")) {
				age18to25.add(user);
			} else if (category.equals("Từ 25 đến 35 tuổi")) {
				from25to35.add(user);
			} else if (category.equals("35 tuổi trở lên")) {
				from35andAbove.add(user);
			}
		}

		// Đếm tổng số lượng tài khoản
		double tongSo = usersList.size();

		// Tính phần trăm độ tuổi
		double tu35troLen = (from35andAbove.size() / tongSo) * 100;

		return Math.round(tu35troLen * 10 + 0.05) / 10.0;
	}

	@GetMapping("/admin/reportlistCountAcc")
	public List<ListAcc> listCountAcc() {
		return listCountAcc.listAccs();
	}

	@GetMapping("/admin/reporttopPostsLikes")
	public List<TopPostLike> topPostLikes() {
		return topLike.topPostLikes();
	}

	@GetMapping("/admin/reportTotalPosts")
	public List<TotalPosts> totalPosts() {
		return totalPosts.totalPosts();
	}

	// Phương thức lọc theo năm
	@GetMapping("/admin/report/filterYearViolationsPosts/{year}")
	public List<ViolationsPosts> filterYearViolationsPosts(@PathVariable int year) {
		// Nhận tham số và thực hiện theo năm đã chọn
		return EXEC.violationsPosts(year);
	}

	@GetMapping("/admin/report/filterYearReport/{year}")
	public List<NumberReport> filterYearReport(@PathVariable int year) {
		// Nhận tham số và thực hiện theo năm đã chọn
		return EXEC2.numberReports(year);
	}

	@GetMapping("/admin/report/detail/{postId}")
	public Posts detailPosts(@PathVariable int postId) {
		return postsService.findPostById(postId);
	}

	// Phương thức để tính độ tuổi
	private static int getAge(Date birthday) {
		// Chuyển đổi từ java.sql.Date sang java.util.Date
		java.util.Date utilDate = new java.util.Date(birthday.getTime());

		// Chuyển đổi từ java.util.Date sang java.time.LocalDate
		LocalDate birthdate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Lấy ngày tháng năm hiện tại
		LocalDate currentDate = LocalDate.now();

		// Sử dụng thư viện Period để tính độ tuổi
		Period period = Period.between(birthdate, currentDate);

		return period.getYears();
	}

	// Phương thức phân loại nhóm tuổi
	private static String getCategory(int age) {
		if (age <= 25) {
			return "Từ 18 đến 25 tuổi";
		} else if (age > 25 && age <= 35) {
			return "Từ 25 đến 35 tuổi";
		} else {
			return "35 tuổi trở lên";
		}
	}
}