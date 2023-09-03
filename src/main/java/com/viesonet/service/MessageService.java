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
		for (Message mess : messages) {
			mess.setStatus("Đã xem");
			messageDao.saveAndFlush(mess);
		}
		return messages;
	}

	// Trong phương thức trong service hoặc controller
	public List<Object> getListUsersMess(String userId) {
		List<Object> result = messageDao.getListUsersMess(userId, "Đã gửi");
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
		// Sau khi hoàn thành vòng lặp đầu, bạn có thể thực hiện vòng lặp thứ hai để cập
		// nhật rowData[6]
		for (Object row : uniqueRows) {
			Object[] modifiedRowData = (Object[]) row;
			if (!modifiedRowData[10].equals("")) {
				if (modifiedRowData[0].equals(userId)) {
					modifiedRowData[6] = "Bạn đã gửi một hình ảnh";
				} else {
					modifiedRowData[6] = "Bạn đã nhận một hình ảnh";
				}
			}
			if (!modifiedRowData[0].equals(userId)) {

				// Đổi giá trị giữa modifiedRowData[0] và modifiedRowData[2]
				Object temp0 = modifiedRowData[0];
				modifiedRowData[0] = modifiedRowData[2];
				modifiedRowData[2] = temp0;

				// Đổi giá trị giữa modifiedRowData[1] và modifiedRowData[3]
				Object temp1 = modifiedRowData[1];
				modifiedRowData[1] = modifiedRowData[3];
				modifiedRowData[3] = temp1;

				// Đổi giá trị giữa modifiedRowData[4] và modifiedRowData[5]
				Object temp4 = modifiedRowData[4];
				modifiedRowData[4] = modifiedRowData[5];
				modifiedRowData[5] = temp4;
			}
			if ("Đã ẩn".equals(modifiedRowData[8])) {
				modifiedRowData[6] = "Tin nhắn đã được thu hồi";
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
