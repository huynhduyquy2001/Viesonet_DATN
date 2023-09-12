package com.viesonet.admin;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viesonet.security.AuthConfig;
import com.viesonet.entity.*;
import com.viesonet.service.SessionService;
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
@Controller
public class ReportController {
		
	@Autowired
	UsersService userService;
	
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
	
	@Autowired
	private AuthConfig authConfig;
	
	//Load dữ liệu khi mở lên
	@GetMapping("/admin/report")
	public String thongKe(Model m, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		//Tìm người dùng vai trò là admin
		m.addAttribute("acc", userService.findUserById(account.getUserId()));
		
		//Thực hiện gọi stored procedure
		List<ViolationsPosts> rs = EXEC.violationsPosts(LocalDate.now().getYear());
		List<NumberReport> rs2 = EXEC2.numberReports(LocalDate.now().getYear());
		
		//Lấy năm
		List<ListYear> list = EXEC1.listYears();
		
		//Truyền dữ liệu
		m.addAttribute("yearNow", LocalDate.now().getYear());
		m.addAttribute("listYear", list);
		m.addAttribute("soluotBaoCao", rs);
		m.addAttribute("soBaiViet", rs2);
		
		// Lấy danh sách các người dùng
		List<Users> usersList = userService.findAll();
		
		//Tạo danh sách nhóm tuổi
		List<Users> age18to25 = new ArrayList<>();
        List<Users> from25to35 = new ArrayList<>();
        List<Users> from35andAbove = new ArrayList<>();
        
        //Chạy vòng lặp để thêm vào từng nhóm
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
        
        //Đếm tổng số lượng tài khoản
        double tongSo = usersList.size();
        
        //Tính phần trăm độ tuổi
        double nhom18den25 = (age18to25.size() / tongSo )* 100;
        double nhom25den35 = (from25to35.size() / tongSo )* 100;
        double tu35troLen = (from35andAbove.size() / tongSo )* 100;
        
        //Đưa dữ liệu qua js để hiển thị
        m.addAttribute("nhom1", Math.round(nhom18den25 * 10 + 0.05) / 10.0); // Math.round() để làm tròn số thập phân
        m.addAttribute("nhom2", Math.round(nhom25den35 * 10 + 0.05) / 10.0);
        m.addAttribute("nhom3", Math.round(tu35troLen * 10 + 0.05) / 10.0);
        
        //Thống kê số accounts mới tham gia trong 7 ngày qua
        List<ListAcc> listAc = listCountAcc.listAccs();
        
        //Truyền dữ liệu
        m.addAttribute("listAcc", listAc);
        
        //Thống kê top 5 bài viết được yêu thích
        List<TopPostLike> listTopLike = topLike.topPostLikes();
        
        //Truyền dữ liệu
        m.addAttribute("topLike", listTopLike);
        
        //Thống kê tài khoản có nhiều bài viết nhất
        List<TotalPosts> tPosts = totalPosts.totalPosts();
        //Truyền dữ liệu
        m.addAttribute("TotalPosts", tPosts); 
		return "/admin/report";
	}
	
	
	//Phương thức lọc theo năm
	@ResponseBody
	@RequestMapping("/admin/report/filterYear/{year}")
	public ReportAndViolations filterYear(@PathVariable int year){
		//Nhận tham số và thực hiện theo năm đã chọn
		List<ViolationsPosts> rs = EXEC.violationsPosts(year);
		List<NumberReport> rs2 = EXEC2.numberReports(year);
		
		//Trả về list chứa 2 danh sách
		return new ReportAndViolations(rs,rs2);
	}
	
	//Phương thức xem chi tiết bài viết
	@ResponseBody
	@RequestMapping("/admin/report/detail/{postId}")
	public TopPostLike detailPosts(Model m, @PathVariable int postId) {
		return filterPostsLike.topPostsLike(postId);
	}
	
	 //Phương thức để tính độ tuổi
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
	 
	 //Phương thức phân loại nhóm tuổi
	 private static String getCategory(int age) {
	        if (age <= 25) {
	            return "Từ 18 đến 25 tuổi";
	        } else if (age > 25 && age <=35) {
	            return "Từ 25 đến 35 tuổi";
	        } else {
	            return "35 tuổi trở lên";
	        }
	    }
}
