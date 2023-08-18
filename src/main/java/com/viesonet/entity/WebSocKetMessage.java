package com.viesonet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class WebSocKetMessage {
	private String content;
	private String sender;
	private String receiver;
	private String avatar;
	private MessageType type;

	public enum MessageType {
		CHAT, LEAVE, JOIN
	}
}
