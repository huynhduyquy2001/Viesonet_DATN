package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Follow;
import com.viesonet.entity.Users;


public interface FollowDao extends JpaRepository<Follow, Integer> {
	@Query("SELECT obj from Follow obj where obj.follower.userId = ?1")
    List<Follow> findByFollowingId(String followingId);
	
	@Query("SELECT obj from Follow obj where obj.follower.userId = ?1")
    List<Follow> findByFollowersId(String followersId);
	
	@Query("SELECT COUNT(obj)  from Follow obj where obj.following = ?1")
    int getFollowersById(Users user);
	
	@Query("SELECT COUNT(obj) from Follow obj where obj.follower = ?1")
    int getFollowingById(Users user);
	
	@Query("SELECT f.follower FROM Follow f JOIN f.following u WHERE u.userId = :userId")
	List<Users> findFollowersInfoByUserId(String userId);
	
	@Query("SELECT f.following FROM Follow f JOIN f.follower u JOIN f.following WHERE u.userId = :userId")
	List<Users> findFollowingInfoByUserId(String userId);	
	
	@Query("SELECT f.following.userId FROM Follow f JOIN f.follower u WHERE u.userId = :userId")
    List<String> findUserIdsOfFollowing(@Param("userId") String userId);
	
	void deleteByFollowerAndFollowing(Users follower, Users following);
	
	
	
}
