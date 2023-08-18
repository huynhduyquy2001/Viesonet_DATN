package com.viesonet.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.viesonet.AuthConfig;
import com.viesonet.entity.*;
import com.viesonet.service.*;

import jakarta.transaction.Transactional;



@Controller
public class PostsViolationsController {

	@Autowired
	UsersService userService;
	
	@Autowired
	ViolationsService violationsService;
	
	
	@Autowired
	sp_FilterPostLikeService filterPostsLike;
	
	@Autowired
	private AuthConfig authConfig;
	
	@GetMapping("/admin/postsviolation")
	public String postsViolations(Model m,  @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		// Tìm người dùng vai trò là admin
		m.addAttribute("acc", userService.findUserById(account.getUserId()));
		
		// Lấy danh sách bài viết vi phạm
		m.addAttribute("listPosts", violationsService.findAllListFalse(page, size));
		return "admin/postsviolations";
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping("/admin/postsviolations/detailPost/{postId}")
	public TopPostLike detailPost(@PathVariable int postId) {
		return filterPostsLike.topPostsLike(postId);
	}
	
	@ResponseBody
	@RequestMapping("/admin/postsviolations/detailViolation/{postId}")
	public List<Object> detailViolation(@PathVariable int postId) {
		return violationsService.findList(postId);
	}

	@ResponseBody
	@RequestMapping("/admin/postsviolations/search/{userViolation}")
	public List<Object> searchUserViolation(@PathVariable String userViolation) {
		return violationsService.findSearchUserViolation(userViolation);
	}
	
	@ResponseBody
	@RequestMapping("/admin/postsviolations/delete")
	@Transactional
	public Page<Object> getPostId(@RequestBody List<String> listPostId) {
		 
		violationsService.deleteByPostViolations(listPostId);
        return violationsService.findAllListFalse(0, 9);
	}
	
	
}
