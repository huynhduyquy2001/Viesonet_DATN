package com.viesonet.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
import com.viesonet.service.AccountsService;

import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.mail.internet.MimeMessage;

@RestController
public class ForgotPasswordController {
	@Autowired
	private AccountsService accountsService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private SessionService sessionService;

	private int[] randomNumbers;
	
	 // Khai báo biến thời gian gửi mã cuối cùng
    private long lastSentTime = 0;
    private static final long COOLDOWN_PERIOD = 60 * 1000; // 1 phút

	public int[] getRandomNumbers() {
		Random random = new Random();
		int[] numbers = new int[4];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = random.nextInt(10);
		}
		return numbers;
	}

	@GetMapping("/forgotpassword")
	public ModelAndView getForgotPasswordPage() {
		randomNumbers = getRandomNumbers();
		ModelAndView modelAndView = new ModelAndView("ForgotPassword");
		return modelAndView;
	}

	@PostMapping("/quenmatkhau/guima")
	public ResponseEntity<?> quenmatkhau(@RequestBody Map<String, Object> data) {
		String email = (String) data.get("email");
		String phone = (String) data.get("phone");
		MimeMessage message = sender.createMimeMessage();

		Accounts accounts = accountsService.findByPhoneNumber(phone);
		
		 // Kiểm tra thời gian chờ giữa các lần gửi mã
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSentTime < COOLDOWN_PERIOD) {
            long remainingTime = (lastSentTime + COOLDOWN_PERIOD - currentTime) / 1000;
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Vui lòng đợi " + remainingTime + " giây trước khi gửi lại mã."));
        }

        // Cập nhật thời gian gửi mã cuối cùng
        lastSentTime = currentTime;

		if (!email.equalsIgnoreCase(accounts.getEmail())) {
			return ResponseEntity.ok()
					.body(Collections.singletonMap("message", "Email này không đúng với tài khoản đã đăng kí !"));
		} else if (accounts.getAccountStatus().getStatusId() == 4) {
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Tài khoản này đã bị khóa !"));
		} else {
			Users users = usersService.getById(accounts.getUser().getUserId());
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
				helper.setFrom("VIE_SONET");
				helper.setTo(email);
				helper.setSubject("MẬT MÃ CỦA BẠN - Vie_SoNet");
				randomNumbers = getRandomNumbers();
				helper.setText("XIN CHÀO BẠN " + "<h3>" + users.getUsername() + "</h3>" + "<br>"
						+ "Mình ở đây để gửi mật mã cho bạn và mật mã của bạn là: " + "<h3>"
						+ Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", "") + "<h3> <br>"
						+ "Bạn nhớ là đừng để mật mã mình bị lộ ra ngoài nha!" + "<br>" + "Vie_Sonet THÂN CHÀO!", true);
			} catch (Exception e) {
				return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật mã chưa được gửi !"));
			}
			sender.send(message);
			return ResponseEntity.ok().build();
		}

	}

	@PostMapping("/quenmatkhau/xacNhan")
	public ResponseEntity<?> xacNhan(@RequestBody Map<String, Object> data) {
		String email = (String) data.get("email");
		String code = (String) data.get("matMa");
		Accounts accounts = accountsService.findByEmail(email);
		String expectedCode = Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", "");
		if (!code.equals(expectedCode)) {
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật mã chưa chính xác!"));
		} else {
			sessionService.set("phone", accounts.getPhoneNumber());
			return ResponseEntity.ok().build();
		}
	}
}
