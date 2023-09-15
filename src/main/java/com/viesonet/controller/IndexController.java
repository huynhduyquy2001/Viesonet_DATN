package com.viesonet.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.security.AuthConfig;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Comments;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Reply;
import com.viesonet.entity.ReplyRequest;
import com.viesonet.entity.Users;
import com.viesonet.entity.ViolationTypes;
import com.viesonet.entity.Violations;
import com.viesonet.service.CommentsService;
import com.viesonet.service.CookieService;
import com.viesonet.service.FavoritesService;
import com.viesonet.service.FollowService;
import com.viesonet.service.ImagesService;
import com.viesonet.service.InteractionService;
import com.viesonet.service.NotificationsService;
import com.viesonet.service.PostsService;
import com.viesonet.service.ReplyService;
import com.viesonet.service.UsersService;
import com.viesonet.service.ViolationTypesService;
import com.viesonet.service.ViolationsService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class IndexController {

	@Autowired
	private FollowService followService;

	@Autowired
	private PostsService postsService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private UsersService usersService;

	@Autowired
	ServletContext context;

	@Autowired
	ImagesService imagesService;

	@Autowired
	CommentsService commentsService;

	@Autowired
	InteractionService interactionService;

	@Autowired
	CookieService cookieService;

	@Autowired
	ReplyService replyService;

	@Autowired
	NotificationsService notificationsService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ViolationTypesService violationTypesService;

	@Autowired
	private ViolationsService violationService;

	@GetMapping("/findfollowing")
	public List<Users> getFollowingInfoByUserId() {
		String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
		return followService.getFollowingInfoByUserId(phoneNumber);
	}

	@GetMapping("/myendpoint")
	public String myEndpoint(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		System.out.println("dang tim:" + token);
		// ... code xử lý JWT token và công việc khác ...
		return token;
	}

	@GetMapping("/get-more-posts/{page}")
	public List<Posts> getMoreFollowedPosts(@PathVariable("page") int page) {
		int postsPerPage = 10;
		int startIndex = (page - 1) * postsPerPage;
		int endIndex = startIndex + postsPerPage;

		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Follow> followList = followService.getFollowing(userId);
		List<String> followedUserIds = followList.stream()
				.map(follow -> follow.getFollowing().getUserId())
				.collect(Collectors.toList());

		List<Posts> allFollowedPosts = postsService.findPostsByListUserId(followedUserIds); // Thay vì lấy toàn bộ bài
																							// viết, chỉ lấy bài viết
																							// của người được theo dõi

		if (startIndex >= allFollowedPosts.size()) {
			return Collections.emptyList();
		}

		return allFollowedPosts.subList(startIndex, Math.min(endIndex, allFollowedPosts.size()));
	}

	@ResponseBody
	@GetMapping("/findlikedposts")
	public List<String> findLikedPosts() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		return favoritesService.findLikedPosts(userId);
	}

	@GetMapping("/findmyaccount")
	public AccountAndFollow findMyAccount(Authentication authentication) {
		String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
		return followService.getFollowingFollower(usersService.findUserById(phoneNumber));
	}

	@ResponseBody
	@PostMapping("/likepost/{postId}")
	public void likePost(@PathVariable("postId") int postId) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		// thêm tương tác
		Posts post = postsService.findPostById(postId);
		interactionService.plusInteraction(userId, post.getUser().getUserId());

		// thêm thông báo
		Notifications ns = notificationsService.findNotificationByPostId(post.getUser().getUserId(), 3, postId);
		if (ns == null) {
			Notifications notifications = notificationsService.createNotifications(
					usersService.findUserById(userId), post.getLikeCount(), post.getUser(), post, 3);

			messagingTemplate.convertAndSend("/private-user", notifications);
		}

		favoritesService.likepost(usersService.findUserById(userId), postsService.findPostById(postId));
	}

	@ResponseBody
	@PostMapping("/didlikepost/{postId}")
	public void didlikePost(@PathVariable("postId") int postId) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		Posts post = postsService.findPostById(postId);
		interactionService.minusInteraction(userId, post.getUser().getUserId());
		favoritesService.didlikepost(userId, postId);
	}

	@GetMapping("/postdetails/{postId}")
	public Posts postDetails(@PathVariable("postId") int postId) {
		return postsService.findPostById(postId);
	}

	@PostMapping("/addcomment/{postId}")
	public Comments addComment(@PathVariable("postId") int postId, @RequestParam("myComment") String content) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		// thêm tương tác
		Posts post = postsService.findPostById(postId);

		interactionService.plusInteraction(userId, post.getUser().getUserId());

		// thêm thông báo
		Notifications notifications = notificationsService.createNotifications(
				usersService.findUserById(userId), post.getCommentCount(), post.getUser(), post, 4);

		messagingTemplate.convertAndSend("/private-user", notifications);

		return commentsService.addComment(postsService.findPostById(postId), usersService.findUserById(userId),
				content);
	}

	@PostMapping("/addreply")
	public ResponseEntity<Reply> addReply(@RequestBody ReplyRequest request, Authentication authentication) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		// Lấy các tham số từ request
		String receiverId = request.getReceiverId();
		String replyContent = request.getReplyContent();
		int commentId = request.getCommentId();
		int postId = request.getPostId();
		System.out.println("postId :" + postId);

		// thêm thông báo
		Users user = usersService.findUserById(receiverId);
		Posts post = postsService.findPostById(postId);
		Notifications notifications = notificationsService
				.createNotifications(usersService.findUserById(userId), 0, user, post, 6);
		messagingTemplate.convertAndSend("/private-user", notifications);

		return ResponseEntity.ok(replyService.addReply(usersService.findUserById(userId), replyContent,
				commentsService.getCommentById(commentId), usersService.findUserById(receiverId),
				postsService.findPostById(postId)));

	}

	@GetMapping("/findpostcomments/{postId}")
	public List<Comments> findPostComments(@PathVariable("postId") int postId) {
		return commentsService.findCommentsByPostId(postId);
	}

	@PostMapping("/post")
	public String dangBai(@RequestParam("imagesUrl") List<String> imagesUrl,
			@RequestParam("content") String content) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById(userId), content);
		// Lưu hình ảnh vào thư mục static/images
		System.out.println("Oke con de:" + imagesUrl.size());
		for (int i = 0; i < imagesUrl.size(); i++) {
			System.out.println(imagesUrl.get(i));
			if (imagesUrl.get(i)
					.startsWith("https://firebasestorage.googleapis.com/v0/b/viesonet-datn.appspot.com/o/images")) {
				imagesService.saveImage(myPost, imagesUrl.get(i), true);
			} else {
				imagesService.saveImage(myPost, imagesUrl.get(i), false);
			}
		}

		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}

	@GetMapping("/loadnotification")
	public List<Notifications> getNotification() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Notifications> n = notificationsService.findNotificationByReceiver(userId);
		if (n.isEmpty()) {
			return null;
		} else {
			return n;
		}
	}

	@GetMapping("/loadallnotification")
	public List<Notifications> getAllNotification() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		return notificationsService.findAllByReceiver(userId); // Implement hàm này để lấy thông báo từ CSDL
	}

	@PostMapping("/setHideNotification")
	public void setHideNotification(@RequestBody List<Notifications> notification) {
		if (!notification.isEmpty()) {
			notificationsService.setFalseNotification(notification);
		}
	}

	@DeleteMapping("/deleteNotification/{notificationId}")
	public void deleteNotification(@PathVariable int notificationId) {
		notificationsService.deleteNotification(notificationId);
	}

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public ModelAndView getHomePage() {
		ModelAndView modelAndView = new ModelAndView("Index");
		return modelAndView;
	}

	@GetMapping("/getviolations")
	public List<ViolationTypes> getViolations() {
		return violationTypesService.getViolations();
	}

	@PostMapping("/report/{postId}/{violationTypeId}")
	public Violations report(@PathVariable("postId") int postId, @PathVariable("violationTypeId") int violationTypeId) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		return violationService.report(usersService.getUserById(userId), postsService.findPostById(postId),
				violationTypesService.getById(violationTypeId));
	}

	@GetMapping("/error")
	public ModelAndView getAccessDenied() {
		ModelAndView modelAndView = new ModelAndView("error");
		return modelAndView;
	}

}
