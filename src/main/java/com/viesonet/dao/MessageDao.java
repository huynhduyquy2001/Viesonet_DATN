package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Message;
import com.viesonet.entity.UserMessage;

public interface MessageDao extends JpaRepository<Message, Integer> {

	@Query("""
			Select obj from Message obj where (obj.sender.userId=?1 and obj.receiver.userId=?2) or\
			(obj.sender.userId=?2 and obj.receiver.userId=?1)\
			""")
	List<Message> getListMess(String senderId, String receiverId);

	@Query("SELECT DISTINCT m.sender.userId, m.sender.username, m.receiver.userId, m.receiver.username, m.sender.avatar, m.receiver.avatar, m.content,  m.sendDate, m.status, m.messId, m.image,(SELECT COUNT(*) FROM Message subm WHERE subm.status =?2 AND subm.receiver.userId = ?1 GROUP BY subm.sender.userId)  FROM Message m WHERE m.receiver.userId = ?1 OR m.sender.userId = ?1 order by m.sendDate desc ")
	List<Object> getListUsersMess(String userId, String status);

	@Query("SELECT count(*) FROM Message list where list.receiver.userId = ?1 and list.status = ?2")
	int getListUnseenMessage(String userId, String chuoi);

	@Query("SELECT count(*) FROM Message m WHERE m.receiver.userId = ?1 AND m.status = ?2 GROUP BY m.sender.userId")
	List<Integer> getListUnseen(String userId, String chuoi);

	@Query("SELECT list FROM Message list where list.sender.userId = ?1 and list.receiver.userId = ?2  and list.status = ?3")
	List<Message> getListMessByReceiverId(String senderId, String receiverId, String status);

}
