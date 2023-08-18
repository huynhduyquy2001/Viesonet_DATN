package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.*;

public interface ViolationsDao extends JpaRepository<Violations, Integer> {

	@Query("SELECT v.violationType.violationDescription, COUNT(v.violationType.violationTypeId) FROM Violations v WHERE v.post.postId = :postId GROUP BY v.violationType.violationDescription")
	List<Object> findList(@Param("postId") int postId);

	@Query("SELECT DISTINCT v.post.postId, v.post.content, v.post.user.username, v.post.postDate FROM Violations v WHERE v.violationStatus = false")
	Page<Object> findAllListFalse(Pageable pageable);
	
	@Query("SELECT DISTINCT v.post.postId, v.post.content, v.post.user.username, v.post.postDate FROM Violations v WHERE v.violationStatus = false and v.post.user.username LIKE %:username%")
	List<Object> findSearchUserViolation(@Param("username") String username);
	
	@Query("SELECT DISTINCT v.post.postId, v.post.content, v.post.user.username, v.post.postDate FROM Violations v WHERE v.violationStatus = true and v.post.user.username LIKE %:username%")
	List<Object> findSearchUserViolations(@Param("username") String username);
	
	@Query("SELECT v FROM Violations v WHERE v.post.postId = :postId")
	List<Violations> findByPostId(@Param("postId") int postId);
	
	@Query("SELECT DISTINCT v.post.postId, v.post.content, v.post.user.username, v.post.postDate FROM Violations v WHERE v.violationStatus = true")
	Page<Object> findViolationsWithStatusTrue(Pageable pageable);

}
