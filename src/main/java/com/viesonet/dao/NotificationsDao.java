package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Notifications;

public interface NotificationsDao extends JpaRepository<Notifications, Integer> {
	@Query("SELECT n FROM Notifications n WHERE n.notificationStatus = true AND n.receiver.userId =:userId")
	List<Notifications> findNotificationTrue(String userId, Sort sort);
	
	@Query("SELECT n FROM Notifications n WHERE n.receiver.userId =:userId")
	List<Notifications> findAllByReceiver(String userId, Sort sort);
	
	@Query("SELECT n FROM Notifications n WHERE n.receiver.userId = :userId AND n.notificationType.typeId = :notificationType AND n.post.postId = :postId")
	Notifications findNotificationByPostId(String userId, int notificationType, int postId);

	Notifications findByNotificationId(int notificationId);
}
