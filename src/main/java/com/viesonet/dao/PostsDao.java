package com.viesonet.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Posts;

public interface PostsDao extends JpaRepository<Posts, Integer>{
	@Query("SELECT b FROM Posts b WHERE b.user.userId IN :userId AND b.isActive=true")
	List<Posts> findPostsByListUserId(List<String> userId, Sort sort);
	
	@Query("SELECT b FROM Posts b WHERE b.user.userId = ?1 AND b.isActive=true")
	List<Posts> getMyPosts(String userId, Sort sort);
	
	@Query("SELECT b FROM Posts b WHERE b.user.userId = ?1 AND b.isActive=true")
	Page<Object> find9Post(Pageable pageable, String userId);

	@Query("SELECT COUNT(b) FROM Posts b WHERE b.user.userId = :userId")
    Integer countMyPosts(String userId);
	
	
	@Query("SELECT b FROM Posts b WHERE b.user.userId = :userId")
	List<Posts> findByUserId(@Param("userId") String userId);

}
	