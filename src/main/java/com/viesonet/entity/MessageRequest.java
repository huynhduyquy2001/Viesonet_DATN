package com.viesonet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Lớp MessageRequest để chứa thông tin tin nhắn gửi từ client
public class MessageRequest {
	String senderId;
	private String receiverId;
	private String content;
	
}
