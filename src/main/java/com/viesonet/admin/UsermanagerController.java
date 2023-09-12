package com.viesonet.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
import com.viesonet.service.AccountsService;
import com.viesonet.service.UsersService;

@RestController
public class UsermanagerController {

	@Autowired
	UsersService userDAO;

	@Autowired
	AccountsService accountService;

	@GetMapping("/admin/usermanager")
	public ModelAndView getHomePage() {
		ModelAndView modelAndView = new ModelAndView("/admin/usermanager");
		return modelAndView;
	}

	@GetMapping("/admin/usermanager/load")
	public List<Object> usermanager() {
		return userDAO.findByAll();
	}

	@GetMapping("/admin/usermanager/profile")
	public Users profile() {
		return userDAO.findUserById("UI001");
	}

	@GetMapping("/admin/usermanager/search/{key}")
	public List<Object> search(@PathVariable String key) {
		return userDAO.findByUserSearch(key);
	}

	@GetMapping("/admin/usermanager/searchId/{key}")
	public Users searchId(@PathVariable String key) {
		return userDAO.findUserById(key);
	}

	@GetMapping("/admin/usermanager/detailUser/{userId}")
	public Users detailUser(@PathVariable String userId) {
		// Tìm thông tin chi tiết người dùng
		return userDAO.findUserById(userId);
	}

	@PutMapping("/admin/usermanager/userRole/{sdt}/{role}")
	public Users userRole(@PathVariable int role, @PathVariable String sdt) {
		String roleName = "";
		if (role == 2) {
			roleName = "Staff";
		} else if (role == 3) {
			roleName = "User";
		}
		Accounts accounts = new Accounts();
		accounts = accountService.setRole(sdt, role, roleName);
		accounts = accountService.findByPhoneNumber(sdt);
		return userDAO.findUserById(accounts.getUser().getUserId());
	}

	@PutMapping("/admin/usermanager/userViolations/{userId}")
	public Users userViolations(@PathVariable String userId) {
		userDAO.setViolationCount(userId);
		return userDAO.findUserById(userId);
	}
}
