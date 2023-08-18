package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.viesonet.dao.NotificationsDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.NotificationType;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

@Service
public class NotificationsService {
	@Autowired
	NotificationsDao notificationsDao;
	
	@Autowired
	UsersDao usersDao;
	
	public Notifications createNotifications(Users username, int count, Users receiverId, Posts post, int notificationType) {
		Notifications notifications = new Notifications();
		Users user = new Users();
		if(notificationType == 1) {
			notifications.setNotificationContent(username.getUsername() + " vừa đăng một bài viết mới");
		}else if(notificationType == 2) {
			notifications.setNotificationContent(username.getUsername() +  " đã bắt đầu follow bạn");
		}else if(notificationType == 3) {
			if(count == 0) {
				notifications.setNotificationContent(username.getUsername() + " đã thích bài viết của bạn");
			}else {
					notifications.setNotificationContent(username.getUsername() + " và " + count + " người khác đã thích bài viết của bạn");
			}
		}else if(notificationType == 4) {
			if(count == 0) {
				notifications.setNotificationContent(username.getUsername() + " đã bình luận bài viết của bạn");
			}else {
				notifications.setNotificationContent(username.getUsername() + " và " + count + " người khác đã bình luận bài viết của bạn");
			}
		}else if(notificationType == 5) {
			notifications.setNotificationContent("Bài viết của bạn đã bị phạm!");
		}else if(notificationType == 6) {
			notifications.setNotificationContent(username.getUsername() + " đã trả lời bình luận của bạn");
		}
		user.setUserId(receiverId.getUserId());
		user.setAvatar(receiverId.getAvatar());
		notifications.setReceiver(user);
		notifications.setPost(post);
		NotificationType nT = new NotificationType();
		nT.setTypeId(notificationType);
		notifications.setNotificationType(nT);
		Date date = new Date();
		notifications.setNotificationDate(date);
		notifications.setNotificationStatus(true);
		
		return notificationsDao.saveAndFlush(notifications);
	}
	
	public List<Notifications> findAllByReceiver(String userId){
		return notificationsDao.findAllByReceiver(userId, Sort.by(Sort.Direction.DESC, "notificationDate"));
	}
	
	public List<Notifications> findNotificationByReceiver(String userId){
		return notificationsDao.findNotificationTrue(userId, Sort.by(Sort.Direction.DESC, "notificationDate"));
	}
	
	public Notifications findNotificationById(int notificationId) {
		Optional<Notifications> optionalNotification = notificationsDao.findById(notificationId);
		return optionalNotification.orElse(null);
	}
	
	public void setFalseNotification(List<Notifications> notifications) {
		for(Notifications ns : notifications) {
			Notifications notification = notificationsDao.findByNotificationId(ns.getNotificationId());
			notification.setNotificationStatus(false);
			notificationsDao.saveAndFlush(notification);
		}
	}
	
	public void deleteNotification(int notificationId) {
		notificationsDao.delete(notificationsDao.findByNotificationId(notificationId));
	}
	
	public Notifications findNotificationByPostId(String userId, int notificationType, int postId) {
		return notificationsDao.findNotificationByPostId(userId, notificationType, postId);
	}
}
