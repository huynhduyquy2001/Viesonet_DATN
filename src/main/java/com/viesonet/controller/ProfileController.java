package com.viesonet.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Follow;
import com.viesonet.entity.FollowDTO;
import com.viesonet.entity.Images;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;
import com.viesonet.entity.ViolationTypes;
import com.viesonet.entity.Violations;
import com.viesonet.security.AuthConfig;
import com.viesonet.service.AccountsService;
import com.viesonet.service.FollowService;
import com.viesonet.service.ImagesService;
import com.viesonet.service.PostsService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;
import com.viesonet.service.ViolationTypesService;
import com.viesonet.service.ViolationsService;

import jakarta.servlet.ServletContext;
import net.coobird.thumbnailator.Thumbnails;

@RestController
public class ProfileController {

	@Autowired
	private FollowService followService;

	@Autowired
	private PostsService postsService;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private ImagesService imagesService;

	@Autowired
	ServletContext context;

	@Autowired
	SessionService session;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ViolationTypesService violationTypesService;

	@Autowired
	private ViolationsService violationService;

	@Autowired
	private AuthConfig authConfig;

	// Lấy thông tin về follow người dùng hiện tại
	@GetMapping("/findmyfollow")
	public AccountAndFollow findMyAccount() {
		String account = SecurityContextHolder.getContext().getAuthentication().getName();
		return followService.getFollowingFollower(usersService.findUserById(account));
	}

	// Lấy thông tin chi tiết các followers
	@GetMapping("/findmyfollowers/{userId}")
	public List<Users> getFollowersInfoByUserId(@PathVariable String userId) {
		return followService.getFollowersInfoByUserId(userId);
	}

	// Lấy thông tin chi tiết các followings
	@GetMapping("/findmyfollowing/{userId}")
	public List<Users> getFollowingInfoByUserId(@PathVariable String userId) {
		return followService.getFollowingInfoByUserId(userId);
	}

	// Lấy thông tin chi tiết của người dùng trong bảng Users
	@GetMapping("/findusers")
	public Users findmyi1(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		return usersService.findUserById(account.getUserId());
	}

	// Lấy thông tin chi tiết của người dùng trong bảng Account

	// Lấy danh sách video theo UserId
	@GetMapping("/getListVideo/{userId}")
	public List<Images> getVideosByUserId(@PathVariable String userId) {
		return imagesService.getVideosByUserId(userId);
	}

	@GetMapping("/findaccounts/{userId}")
	public Accounts findmyi2(@PathVariable String userId) {
		return accountsService.getAccountByUsers(userId);
	}

	@GetMapping("/findmyusers")
	public Users findmyi2(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		return usersService.findUserById(account.getUserId());
	}

	// Đếm số bài viết của người dùng hiện tại
	@GetMapping("/countmypost/{userId}")
	public int countMyPosts(@PathVariable String userId) {
		return postsService.countPost(userId);
	}

	// Phương thức này trả về thông tin người dùng (Users) dựa vào session attribute
	// "id".
	@GetMapping("/getUserInfo")
	public Users getUserInfo(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		return usersService.getUserById(account.getUserId());
	}

	// Phương thức này trả về thông tin tài khoản (Accounts) dựa vào session
	// attribute "id".
	@GetMapping("/getAccInfo")
	public Accounts getAccInfo(Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		return accountsService.getAccountById(account.getUserId());
	}

	// Phương thức này thực hiện cập nhật thông tin người dùng (Users) dựa vào dữ
	// liệu từ request body.
	@PostMapping("/updateUserInfo")
	public void updateUserInfo(@RequestBody Users userInfo) {
		usersService.updateUserInfo(userInfo);
	}

	// Phương thức này thực hiện cập nhật thông tin tài khoản (Accounts) dựa vào dữ
	// liệu từ các path variable email và statusId.
	@PostMapping("/updateAccInfo/{email}/{statusId}")
	public void updateAccInfo(@PathVariable String email, @PathVariable String statusId,
			Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		int id = 0;
		if (statusId.equals("Công khai")) {
			id = 1;
		} else if (statusId.equals("Chỉ theo dõi")) {
			id = 2;
		} else if (statusId.equals("Tạm ẩn")) {
			id = 3;
		}
		accountsService.updateAccInfo(account.getUserId(), email, id);
	}

	// Lấy danh sách follow
	@GetMapping("/getallfollow")
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

	// Nút follow
	@PostMapping("/followOther")
	public List<FollowDTO> followUser(@RequestBody FollowDTO followDTO) {
		// Lấy dữ liệu người dùng hiện tại và người dùng đang được follow
		Users follower = usersService.findUserById(followDTO.getFollowerId());
		Users following = usersService.findUserById(followDTO.getFollowingId());

		// Thêm dữ liệu follow vào cơ sở dữ liệu
		Follow follow = new Follow();
		follow.setFollower(follower);
		follow.setFollowing(following);
		follow.setFollowDate(new Date());

		followService.saveFollow(follow);

		List<Follow> listFollow = followService.findAllFollow();
		List<FollowDTO> listFollowDTO = new ArrayList<>();

		for (Follow follow1 : listFollow) {
			FollowDTO followDTO1 = new FollowDTO();
			followDTO1.setFollowId(follow1.getFollowId());
			followDTO1.setFollowerId(follow1.getFollower().getUserId());
			followDTO1.setFollowingId(follow1.getFollowing().getUserId());
			followDTO1.setFollowDate(follow1.getFollowDate());

			listFollowDTO.add(followDTO1);
		}
		return listFollowDTO;
	}

	// Nút unfollow
	@ResponseBody
	@DeleteMapping("/unfollowOther")
	public void unfollowUser(@RequestBody FollowDTO followDTO) {
		// Lấy dữ liệu người dùng hiện tại và người dùng đang được unfollow
		Users follower = usersService.findUserById(followDTO.getFollowerId());
		Users following = usersService.findUserById(followDTO.getFollowingId());

		// Xóa dữ liệu follow từ cơ sở dữ liệu
		followService.deleteFollowByFollowerAndFollowing(follower, following);
	}

	// Cập nhật ảnh bìa
	@ResponseBody
	@PostMapping("/updateBackground")
	public String doiAnhBia(@RequestParam("photoFiles2") MultipartFile[] photoFiles,
			@RequestParam("content") String content, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById(account.getUserId()), content);
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
					// Cập nhật ảnh bìa cho người dùng
					if (myPost != null) {
						String newBackgroundImageUrl = newFileName;
						usersService.updateBackground(account.getUserId(), newBackgroundImageUrl);
					}
				}

			}
		}
		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}

	// Cập nhật ảnh đại diện
	@ResponseBody
	@PostMapping("/updateAvatar")
	public String doiAnhDaiDien(@RequestParam("photoFiles3") MultipartFile[] photoFiles,
			@RequestParam("content") String content, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById(account.getUserId()), content);
		// Lưu hình ảnh vào thư mục static/images
		if (photoFiles != null && photoFiles.length > 0) {
			for (MultipartFile photoFile : photoFiles) {
				if (!photoFile.isEmpty()) {
					// Tạo tên file ảnh
					String originalFileName = photoFile.getOriginalFilename();
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String newFileName = originalFileName + "-" + timestamp + extension;
					// Tạo đường dẫn để lưu trữ
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
					// Cập nhật ảnh bìa cho người dùng
					if (myPost != null) {
						String newAvatarImageUrl = newFileName;
						usersService.updateAvatar(account.getUserId(), newAvatarImageUrl);
					}
				}

			}
		}
		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}

	// Cập nhật bài viết
	@PutMapping("/updatePost/{postId}")
	public void updatePost(@PathVariable int postId, @RequestBody Posts posts) {
		Posts existingPost = postsService.getPostById(postId);

		existingPost.setContent(posts.getContent());
		postsService.savePost(existingPost);
	}

	// Ẩn bài viết
	@PutMapping("/hide/{postId}")
	public void hidePost(@PathVariable int postId) {
		postsService.hidePost(postId);
	}

	// ----------------------------OtherProfile-----------------------------
	// Lấy thông tin người dùng khác
	@PostMapping("/getOtherUserId/{userId}")
	public Users getOtherUserId(@PathVariable String userId) {
		return usersService.findUserById(userId);
	}

	// Đếm số bài viết của người dùng khác
	@PostMapping("/countmypost/{userId}")
	public int countOtherPosts(@PathVariable String userId) {
		return postsService.countPost(userId);
	}

	// Lấy thông tin về follow người dùng khác
	@PostMapping("/findmyfollow/{userId}")
	public AccountAndFollow findOtherAccount(@PathVariable String userId) {
		return followService.getFollowingFollower(usersService.findUserById(userId));
	}

	// Lấy thông tin các bài viết người dùng khác
	@PostMapping("/getmypost/{userId}")
	public List<Posts> getMyPost(@PathVariable String userId) {
		return postsService.getMyPost(userId);
	}

	// Lấy thông tin chi tiết các followers
	@PostMapping("/findmyfollowers/{userId}")
	public List<Users> getFollowersInfoByOtherId(@PathVariable String userId) {
		return followService.getFollowersInfoByUserId(userId);
	}

	// Lấy thông tin chi tiết các followings
	@PostMapping("/findmyfollowing/{userId}")
	public List<Users> getFollowingInfoByOtherId(@PathVariable String userId) {
		return followService.getFollowingInfoByUserId(userId);
	}

	// Lấy danh sách follow
	@PostMapping("/getallfollow/{userId}")
	public List<FollowDTO> getFollow(@PathVariable String userId) {
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

	// Lấy danh sách ảnh theo UserId
	@GetMapping("/getListImage/{userId}")
	public List<Images> getImagesByUserId(@PathVariable String userId) {
		return imagesService.getImagesByUserId(userId);
	}

	@GetMapping("/user/getviolations")
	public List<ViolationTypes> getViolations() {
		return violationTypesService.getViolations();
	}

	@PostMapping("/user/report/{postId}/{violationTypeId}")
	public Violations report(@PathVariable("postId") int postId, @PathVariable("violationTypeId") int violationTypeId,
			Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);

		String userId = account.getUserId();
		return violationService.report(usersService.getUserById(userId), postsService.findPostById(postId),
				violationTypesService.getById(violationTypeId));
	}

	// @PostMapping("/shop/{userId}")
	// public Users getOtherShop(@PathVariable String userId) {
	// return usersService.findUserById(userId);
	// }

	// @PostMapping("/shop/")
}
