package com.viesonet.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.viesonet.security.AuthConfig;
import com.viesonet.entity.*;
import com.viesonet.service.*;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@CrossOrigin("*")
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
	public Page<Object> postsViolations(Model m, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "9") int size, Authentication authentication) {
		// Lấy danh sách bài viết vi phạm
		return violationsService.findAllListFalse(page, size);
	}

	@RequestMapping("/admin/postsviolations/detailPost/{postId}")
	public TopPostLike detailPost(@PathVariable int postId) {
		return filterPostsLike.topPostsLike(postId);
	}

	@RequestMapping("/admin/postsviolations/detailViolation/{postId}")
	public List<Object> detailViolation(@PathVariable int postId) {
		return violationsService.findList(postId);
	}

	@RequestMapping("/admin/postsviolations/search/{userViolation}")
	public List<Object> searchUserViolation(@PathVariable String userViolation) {
		return violationsService.findSearchUserViolation(userViolation);
	}

	@RequestMapping("/admin/postsviolations/delete")
	public Page<Object> getPostId(@RequestBody List<String> listPostId) {

		violationsService.deleteByPostViolations(listPostId);
		return violationsService.findAllListFalse(0, 9);
	}

}
