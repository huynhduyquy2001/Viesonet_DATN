package com.viesonet.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
	private PostsService postsService;

	@Autowired
	ViolationsService violationsService;

	@Autowired
	sp_FilterPostLikeService filterPostsLike;

	@GetMapping("/admin/postsviolation")
	public ResponseEntity<Page<Object>> postsViolations(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "9") int size) {
		Page<Object> result = violationsService.findAllListFalse(page, size);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/admin/postsviolations/detailPost/{postId}")
	public Posts getViolationsByPostId(@PathVariable int postId) {
		return postsService.findPostById(postId);
	}

	@GetMapping("/admin/searchUserViolation")
	public ResponseEntity<List<Object>> searchUserViolation(@RequestParam String username) {
		List<Object> searchResult = violationsService.findSearchUserViolation(username);
		return ResponseEntity.ok(searchResult);
	}

	@RequestMapping("/admin/postsviolations/delete")
	public ResponseEntity<Page<Object>> deletePostViolations(@RequestBody List<String> listPostId) {
		violationsService.deleteByPostViolations(listPostId);
		Page<Object> page = violationsService.findAllListFalse(0, 9);
		return ResponseEntity.ok(page);
	}
}
