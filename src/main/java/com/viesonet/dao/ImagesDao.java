package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Images;
import com.viesonet.entity.Posts;

public interface ImagesDao extends JpaRepository<Images, Integer> {
	
	// Custom query to find images by userId
    @Query("SELECT i FROM Images i JOIN i.post p JOIN p.user u WHERE u.userId = :userId AND post.isActive = true AND i.type = True")
    List<Images> findImagesByUserId(String userId);
    
    @Query("SELECT i FROM Images i JOIN i.post p JOIN p.user u WHERE u.userId = :userId AND post.isActive = true AND i.type = False")
    List<Images> findVideosByUserId(String userId);
}
	