package com.viesonet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.service.NotificationsService;
import com.viesonet.service.PostsService;
import com.viesonet.service.UsersService;
import com.viesonet.service.ViolationsService;

import jakarta.transaction.Transactional;

@RestController
public class PostViolationsManagementController {
	@Autowired
	private ViolationsService violationsService;
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	NotificationsService notificationsService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;


	@GetMapping("/staff/post_violations_management")
	public ModelAndView getPage() {
		ModelAndView modelAndView = new ModelAndView("/staff/PostsViolationsManagement");
		return modelAndView;
	}

	@GetMapping("/staff/postsviolations/load")
	public ResponseEntity<Page<Object>> postsViolations(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "9") int size) {
		Page<Object> result = violationsService.findViolationsWithStatusTrue(page, size);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/staff/postsviolations/detailPost/{postId}")
	public Posts getViolationsByPostId(@PathVariable int postId) {
		return postsService.findPostById(postId);

	}

	@GetMapping("/staff/postsviolations/detailViolation/{postId}")
	public List<Object> detailViolation(@PathVariable int postId) {
		return violationsService.findList(postId);
	}

	@PostMapping("/staff/postsviolations/delete")
	@Transactional
	public ResponseEntity<Page<Object>> deletePostViolations(@RequestBody List<String> listPostId) {
		violationsService.deleteByPostViolations(listPostId);
		Page<Object> page = violationsService.findViolationsWithStatusTrue(0, 9);
		return ResponseEntity.ok(page);
	}

	@PostMapping("/staff/postsviolations/accept")
	@Transactional
	public ResponseEntity<Page<Object>> acceptPostViolations(@RequestBody List<String> listPostId) {
		violationsService.acceptByPostViolations(listPostId);
		for (int i = 0; i < listPostId.size(); i++) {
			Posts post = postsService.findPostById(Integer.parseInt(listPostId.get(i)));
			
			//Thêm thông báo
			Notifications notifications = notificationsService.createNotifications(
					post.getUser(), post.getLikeCount(), post.getUser(), post, 5);

			messagingTemplate.convertAndSend("/private-user", notifications);
			
		}
		Page<Object> page = violationsService.findViolationsWithStatusTrue(0, 9);
		return ResponseEntity.ok(page);

	}

	@GetMapping("/staff/searchUserViolation")
	public ResponseEntity<List<Object>> searchUserViolation(@RequestParam String username) {
		List<Object> searchResult = violationsService.findSearchUserViolations(username);
		return ResponseEntity.ok(searchResult);
	}

}
