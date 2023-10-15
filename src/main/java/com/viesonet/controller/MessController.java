package com.viesonet.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Message;
import com.viesonet.entity.MessageRequest;
import com.viesonet.entity.Users;
import com.viesonet.service.MessageService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class MessController {

	@Autowired
	MessageService messageService;

	@Autowired
	UsersService usersService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	// @GetMapping("/mess")
	// public ModelAndView getHomePage() {
	// ModelAndView modelAndView = new ModelAndView("Message");
	// return modelAndView;
	// }

	@MessageMapping("/sendnewmess")
	@SendToUser("/queue/receiveMessage")
	public void addMess(Message newMessage) {
		messagingTemplate.convertAndSendToUser(newMessage.getSender().getUserId(), "/queue/receiveMessage",
				newMessage);
		messagingTemplate.convertAndSendToUser(newMessage.getReceiver().getUserId(), "/queue/receiveMessage",
				newMessage);
	}

	@PostMapping("/savemess")
	public Message saveMess(@RequestBody MessageRequest messageRequest) {
		// Thêm tin nhắn vào cơ sở dữ liệu
		Message newMessage = messageService.addMess(usersService.findUserById(messageRequest.getSenderId()),
				usersService.findUserById(messageRequest.getReceiverId()), messageRequest.getContent(), "");
		return newMessage;
	}

	@PostMapping("/sendimage/{receiverId}")
	public List<Message> sendImage(@RequestParam List<String> mediaUrl,
			@PathVariable String receiverId) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Message> list = new ArrayList<>();
		if (mediaUrl != null && mediaUrl.size() > 0) {
			for (String photoFile : mediaUrl) {
				Message newMessage = new Message();
				newMessage = messageService.addMess(usersService.findUserById(userId),
						usersService.findUserById(receiverId), "", photoFile);
				list.add(newMessage);
			}
		}
		return list;
	}

	@GetMapping("/getmess2/{userId}")
	public List<Message> getListMess2(@PathVariable String userId) {
		String myId = SecurityContextHolder.getContext().getAuthentication().getName();
		return messageService.getListMess(myId, userId);
	}

	@GetMapping("/chatlistwithothers")
	public List<Object> getUsersMess() {
		String myId = SecurityContextHolder.getContext().getAuthentication().getName();
		return messageService.getListUsersMess(myId);
	}

	@GetMapping("/getUser/{userId}")
	public Users findUserById(@PathVariable String userId, Model model) {
		return usersService.findUserById(userId);
	}

	@GetMapping("/getunseenmessage")
	public int getListUnseenMessage() {
		String myId = SecurityContextHolder.getContext().getAuthentication().getName();
		return messageService.getListUnseenMessage(myId);
	}

	@PostMapping("/seen/{userId}")
	public List<Message> seen(@PathVariable("userId") String senderId) {
		String myId = SecurityContextHolder.getContext().getAuthentication().getName();
		return messageService.seen(senderId, myId);
	}

	@PostMapping("/removemess/{messId}")
	public Message reMoveMess(@PathVariable int messId) {
		return messageService.removeMess(messageService.getMessById(messId));
	}

	@GetMapping("/mess/{otherId}")
	public ModelAndView loadUserPage(@PathVariable("otherId") String id) {
		ModelAndView modelAndView = new ModelAndView("Message.html");
		modelAndView.addObject("otherId", id);
		return modelAndView;
	}
}
