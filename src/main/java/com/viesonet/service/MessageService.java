package com.viesonet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.MessageDao;
import com.viesonet.entity.Message;
import com.viesonet.entity.UserMessage;
import com.viesonet.entity.Users;

@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;

	public List<Message> getListMess(String senderId, String receiverId) {
		return messageDao.getListMess(senderId, receiverId);
	}

	public Message addMess(Users sender, Users receiver, String content, String image) {
		Message obj = new Message();
		obj.setContent(content);
		obj.setReceiver(receiver);
		obj.setSender(sender);
		obj.setSendDate(new Date());
		obj.setImage(image);
		obj.setStatus("Đã gửi");
		messageDao.save(obj);
		return obj;
	}

	public List<Message> seen(String senderId, String receiverId) {
		List<Message> messages = messageDao.getListMessByReceiverId(senderId, receiverId, "Đã gửi");
		for(Message mess : messages) {
			mess.setStatus("Đã xem");
			messageDao.saveAndFlush(mess);
		}
		return messages;
	}

	// Trong phương thức trong service hoặc controller
	public List<Object> getListUsersMess(String userId) {
		List<Object> result = messageDao.getListUsersMess(userId);
		Set<String> uniquePairs = new HashSet<>();
		List<Object> uniqueRows = new ArrayList<>();

		for (Object row : result) {
			Object[] rowData = (Object[]) row;
			String pair1 = rowData[0] + "-" + rowData[2];
			String pair2 = rowData[2] + "-" + rowData[0];
			if (!uniquePairs.contains(pair1) && !uniquePairs.contains(pair2)) {
				uniqueRows.add(row);
				uniquePairs.add(pair1);
			}
		}

		return uniqueRows;
	}

	public int getListUnseenMessage(String userId) {
		return messageDao.getListUnseenMessage(userId, "Đã gửi");
	}
	public Message removeMess(Message mess) {
		mess.setStatus("Đã ẩn");
		messageDao.saveAndFlush(mess);
		return mess;
	}
	
	public Message getMessById(int messId) {
		Optional<Message> obj = messageDao.findById(messId);
		return obj.orElse(null);
	}

}
