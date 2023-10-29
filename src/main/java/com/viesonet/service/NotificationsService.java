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
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;

@Service
public class NotificationsService {
	@Autowired
	NotificationsDao notificationsDao;

	@Autowired
	UsersDao usersDao;

	public Notifications createNotifications(Users sender, int count, Users receiverId, Posts post, Products products,
			int notificationType) {
		Notifications notifications = new Notifications();
		if (notificationType == 1) {
			notifications.setNotificationContent(sender.getUsername() + " vừa đăng một bài viết mới");
		} else if (notificationType == 2) {
			notifications.setNotificationContent(sender.getUsername() + " đã bắt đầu follow bạn");
		} else if (notificationType == 3) {
			if (count == 0) {
				notifications.setNotificationContent(sender.getUsername() + " đã thích bài viết của bạn");
			} else {
				notifications.setNotificationContent(
						sender.getUsername() + " và " + count + " người khác đã thích bài viết của bạn");
			}
		} else if (notificationType == 4) {
			if (count == 0) {
				notifications.setNotificationContent(sender.getUsername() + " đã bình luận bài viết của bạn");
			} else {
				notifications.setNotificationContent(
						sender.getUsername() + " và " + count + " người khác đã bình luận bài viết của bạn");
			}
		} else if (notificationType == 5) {
			notifications.setNotificationContent("Bài viết của bạn đã bị phạm!");
		} else if (notificationType == 6) {
			notifications.setNotificationContent(sender.getUsername() + " đã trả lời bình luận của bạn");
		}
		notifications.setReceiver(receiverId);
		notifications.setSender(sender);
		notifications.setPost(post);
		notifications.setProduct(products);
		NotificationType nT = new NotificationType();
		nT.setTypeId(notificationType);
		notifications.setNotificationType(nT);
		Date date = new Date();
		notifications.setNotificationDate(date);
		notifications.setNotificationStatus(true);

		return notificationsDao.saveAndFlush(notifications);
	}

	public List<Notifications> findAllByReceiver(String userId) {
		return notificationsDao.findAllByReceiver(userId, Sort.by(Sort.Direction.DESC, "notificationDate"));
	}

	public List<Notifications> findNotificationByReceiver(String userId) {
		return notificationsDao.findNotificationTrue(userId, Sort.by(Sort.Direction.DESC, "notificationDate"));
	}

	public Notifications findNotificationById(int notificationId) {
		Optional<Notifications> optionalNotification = notificationsDao.findById(notificationId);
		return optionalNotification.orElse(null);
	}

	public void setFalseNotification(List<Notifications> notifications) {
		for (Notifications ns : notifications) {
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
