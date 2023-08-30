package com.viesonet.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viesonet.AuthConfig;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Comments;
import com.viesonet.entity.Favorites;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Images;
import com.viesonet.entity.Interaction;
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
import com.viesonet.service.FileChecker;
import com.viesonet.service.FollowService;
import com.viesonet.service.ImagesService;
import com.viesonet.service.InteractionService;
import com.viesonet.service.NotificationsService;
import com.viesonet.service.PostsService;
import com.viesonet.service.ReplyService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;
import com.viesonet.service.ViolationTypesService;
import com.viesonet.service.ViolationsService;

import jakarta.servlet.ServletContext;
import net.coobird.thumbnailator.Thumbnails;

@EnableScheduling
@RestController
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
	private ServletContext servletContext;

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

	@Autowired
	private AuthConfig authConfig;

	@GetMapping("/findfollowing")
	public List<Users> getFollowingInfoByUserId(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		String userId = account.getUserId();
		return followService.getFollowingInfoByUserId(userId);
	}

	@GetMapping("/get-more-posts/{page}")
	public List<Posts> getMoreFollowedPosts(@PathVariable("page") int page, Authentication authentication) {
		int postsPerPage = 10;
		int startIndex = (page - 1) * postsPerPage;
		int endIndex = startIndex + postsPerPage;

		Accounts account = authConfig.getLoggedInAccount(authentication);
		String userId = account.getUserId();
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
	public List<String> findLikedPosts(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		return favoritesService.findLikedPosts(userId);
	}

	@ResponseBody
	@GetMapping("/findmyaccount")
	public AccountAndFollow findMyAccount(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		String userId = account.getUserId();
		return followService.getFollowingFollower(usersService.findUserById(userId));
	}

	@ResponseBody
	@PostMapping("/likepost/{postId}")
	public void likePost(@PathVariable("postId") int postId, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		// thêm tương tác
		Posts post = postsService.findPostById(postId);
		interactionService.plusInteraction(userId, post.getUser().getUserId());

		// thêm thông báo
		Notifications ns = notificationsService.findNotificationByPostId(post.getUser().getUserId(), 3, postId);
		if (ns == null) {
			Notifications notifications = notificationsService.createNotifications(
					usersService.findUserById(account.getUserId()), post.getLikeCount(), post.getUser(), post, 3);

			messagingTemplate.convertAndSend("/private-user", notifications);
		}

		favoritesService.likepost(usersService.findUserById(account.getUserId()), postsService.findPostById(postId));
	}

	@ResponseBody
	@PostMapping("/didlikepost/{postId}")
	public void didlikePost(@PathVariable("postId") int postId, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		String userId = account.getUserId();
		Posts post = postsService.findPostById(postId);
		interactionService.minusInteraction(userId, post.getUser().getUserId());
		favoritesService.didlikepost(userId, postId);
	}

	@GetMapping("/postdetails/{postId}")
	public Posts postDetails(@PathVariable("postId") int postId) {
		return postsService.findPostById(postId);
	}

	@PostMapping("/addcomment/{postId}")
	public Comments addComment(@PathVariable("postId") int postId, @RequestParam("myComment") String content,
			Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		// thêm tương tác
		Posts post = postsService.findPostById(postId);

		interactionService.plusInteraction(userId, post.getUser().getUserId());

		// thêm thông báo
		Notifications notifications = notificationsService.createNotifications(
				usersService.findUserById(account.getUserId()), post.getCommentCount(), post.getUser(), post, 4);

		messagingTemplate.convertAndSend("/private-user", notifications);

		return commentsService.addComment(postsService.findPostById(postId), usersService.findUserById(userId),
				content);
	}

	@PostMapping("/addreply")
	public ResponseEntity<Reply> addReply(@RequestBody ReplyRequest request, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
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
				.createNotifications(usersService.findUserById(account.getUserId()), 0, user, post, 6);
		messagingTemplate.convertAndSend("/private-user", notifications);

		return ResponseEntity.ok(replyService.addReply(usersService.findUserById(account.getUserId()), replyContent,
				commentsService.getCommentById(commentId), usersService.findUserById(receiverId),
				postsService.findPostById(postId)));

	}

	@GetMapping("/findpostcomments/{postId}")
	public List<Comments> findPostComments(@PathVariable("postId") int postId) {
		return commentsService.findCommentsByPostId(postId);
	}

	@ResponseBody
	@PostMapping("/post")
	public String dangBai(@RequestParam("photoFiles") MultipartFile[] photoFiles,
			@RequestParam("content") String content, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		List<String> hinhAnhList = new ArrayList<>();
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById(account.getUserId()), content);

		// Thêm thông báo
		List<Follow> fl = followService.getFollowing(account.getUserId());
		List<Interaction> itn = interactionService.findListInteraction(account.getUserId());
		if (itn.size() == 0) {
			for (Follow list : fl) {
				Notifications notifications = notificationsService.createNotifications(
						usersService.findUserById(account.getUserId()), 0, list.getFollower(), myPost, 1);
				messagingTemplate.convertAndSend("/private-user", notifications);
			}
		} else {
			for (Interaction it : itn) {
				Notifications notifications = notificationsService.createNotifications(
						usersService.findUserById(account.getUserId()), 0, it.getInteractingPerson(), myPost, 1);
				messagingTemplate.convertAndSend("/private-user", notifications);
			}
		}

		// Lưu hình ảnh vào thư mục static/images
		if (photoFiles != null && photoFiles.length > 0) {
			for (MultipartFile photoFile : photoFiles) {
				if (!photoFile.isEmpty()) {
					String originalFileName = photoFile.getOriginalFilename();
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String newFileName = originalFileName + "-" + timestamp + extension;

					String rootPath = servletContext.getRealPath("/");
					String parentPath = new File(rootPath).getParent();
					String pathUpload = parentPath + "/resources/static/images/" + newFileName;

					try {
						photoFile.transferTo(new File(pathUpload));
						String contentType = photoFile.getContentType();
						boolean type = true;
						if (contentType.startsWith("image")) {

						} else if (contentType.startsWith("video")) {
							type = false;
						}
						if (type == true) {
							long fileSize = photoFile.getSize();
							if (fileSize > 1 * 1024 * 1024) {
								double quality = 0.6;
								String outputPath = pathUpload;
								Thumbnails.of(pathUpload).scale(1.0).outputQuality(quality).toFile(outputPath);
							}
						}
						imagesService.saveImage(myPost, newFileName, type);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}

		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}

	@GetMapping("/loadnotification")
	public List<Notifications> getNotification(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		List<Notifications> n = notificationsService.findNotificationByReceiver(account.getUserId());
		if (n.isEmpty()) {
			return null;
		} else {
			return n;
		}
	}

	@GetMapping("/loadallnotification")
	public List<Notifications> getAllNotification(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		String userId = account.getUserId();
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

	// @GetMapping("/logout")
	// public ModelAndView logout() {
	// session.remove("id");
	// session.remove("role");
	// cookieService.delete("user");
	// cookieService.delete("pass");
	// return new ModelAndView("redirect:/login");
	// }

	@GetMapping("/getviolations")
	public List<ViolationTypes> getViolations() {
		return violationTypesService.getViolations();
	}

	@PostMapping("/report/{postId}/{violationTypeId}")
	public Violations report(@PathVariable("postId") int postId, @PathVariable("violationTypeId") int violationTypeId,
			Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		return violationService.report(usersService.getUserById(userId), postsService.findPostById(postId),
				violationTypesService.getById(violationTypeId));
	}

	@GetMapping("/error")
	public ModelAndView getAccessDenied() {
		ModelAndView modelAndView = new ModelAndView("error");
		return modelAndView;
	}

}
