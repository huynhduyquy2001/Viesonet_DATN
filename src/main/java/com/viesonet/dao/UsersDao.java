package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

public interface UsersDao extends JpaRepository<Users, String> {

	Users findByuserId(String userId);

	@Query("SELECT u.userId, u.avatar, u.username, u.account.email, u.violationCount, u.account.email FROM Users u")
	List<Object> findByAll();

	@Query("SELECT u.userId, u.avatar, u.username, u.account.email, u.violationCount, u.account.email FROM Users u WHERE u.username LIKE %:userId%")
	List<Object> findByUserSearch(@Param("userId") String userId);

	@Query("SELECT u.userId, u.avatar, u.username, u.account.email, u.violationCount FROM Users u")
	List<Object> findByUserSearchAll();

	@Query("SELECT u.userId, u.avatar, u.username, u.birthday, u.gender, u.account.phoneNumber, u.relationship, u.account.role.roleName, u.address, u.violationCount, u.account.accountStatus.statusName, u.account.email, u.introduction FROM Users u WHERE u.userId =:userId")
	Object findByDetailUser(@Param("userId") String userId);

	@Query("SELECT b FROM Users b WHERE b.userId=?1")
	List<Posts> findUserByUserId(String userId);

	@Query("SELECT b FROM Users b WHERE b.userId=?1")
	List<Users> findByUser(String userId);

	List<Users> findByUsernameContaining(String username);
}
