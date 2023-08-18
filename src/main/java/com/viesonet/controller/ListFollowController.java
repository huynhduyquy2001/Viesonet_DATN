package com.viesonet.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.service.FollowService;
import com.viesonet.service.SearchService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.websocket.server.PathParam;

import com.viesonet.AuthConfig;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Follow;
import com.viesonet.entity.FollowDTO;
import com.viesonet.entity.Users;

@RestController
public class ListFollowController {
	@Autowired
	private SearchService SearchService;
	@Autowired
	private UsersDao UsersDao;
	@Autowired
	private FollowService followService;

	@Autowired
	private UsersService UsersService;

	@Autowired
	private AuthConfig authConfig;

//	@GetMapping("/listFollow")
//	public ModelAndView getHomePage() {
//		ModelAndView modelAndView = new ModelAndView("ListFollow");
//		return modelAndView;
//	}

	// Lấy thông tin chi tiết các followers
	@GetMapping("/ListFollower")
	public List<Users> getFollowersInfoByUserId(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		return followService.getFollowersInfoByUserId(userId);
	}

	// Lấy thông tin chi tiết các followers
	@GetMapping("/ListFollowing1")
	public List<Users> getFollowingInfoByUserId1(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		return followService.getFollowingInfoByUserId(userId);
	}

	@GetMapping("/ListFollowing")
	public List<String> getFollowingInfoByUserId(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		return followService.findUserIdsOfFollowing(userId);
	}

	@GetMapping("/users")
	public List<Object> getAllUsers() {
		return UsersService.findByUserSearchAll();
	}

	// Nút follow
	@ResponseBody
	@PostMapping("/follow")
	public Follow followUser(@RequestBody FollowDTO followDTO) {
		// Lấy dữ liệu người dùng hiện tại và người dùng đang được follow
		Users follower = UsersService.findUserById(followDTO.getFollowerId());
		Users following = UsersService.findUserById(followDTO.getFollowingId());

		// Thêm dữ liệu follow vào cơ sở dữ liệu
		Follow follow = new Follow();
		follow.setFollower(follower);
		follow.setFollowing(following);
		follow.setFollowDate(new Date());
		return followService.saveFollow(follow);
	}

	// Nút unfollow
	@ResponseBody
	@DeleteMapping("/unfollow")
	public void unfollowUser(@RequestBody FollowDTO followDTO) {
		// Lấy dữ liệu người dùng hiện tại và người dùng đang được unfollow
		Users follower = UsersService.findUserById(followDTO.getFollowerId());
		Users following = UsersService.findUserById(followDTO.getFollowingId());

		// Xóa dữ liệu follow từ cơ sở dữ liệu
		followService.deleteFollowByFollowerAndFollowing(follower, following);
	}
	//Lấy danh sách follow
		@GetMapping("/getfollow")
		public List<FollowDTO> getFollow() {
		    List<Follow> listFollow = followService.findAllFollow();
		    List<FollowDTO> listFollowDTO = new ArrayList<>();

		    for (Follow follow : listFollow) {
		        FollowDTO followDTO = new FollowDTO();
		        followDTO.setFollowId(follow.getFollowId());
		        followDTO.setFollowerId(follow.getFollower().getUserId());
		        followDTO.setFollowingId(follow.getFollowing().getUserId());
		        followDTO.setFollowDate(follow.getFollowDate());

		        listFollowDTO.add(followDTO);
		    }

		    return listFollowDTO;	
		}
}
