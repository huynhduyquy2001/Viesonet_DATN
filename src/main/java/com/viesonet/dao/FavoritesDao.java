package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Favorites;

public interface FavoritesDao extends JpaRepository<Favorites, Integer> {
	@Query("Select obj.post.postId from Favorites obj where obj.user.userId=?1")
	List<String> findLikedPosts(String userId);
	
	@Query("SELECT obj from Favorites obj where obj.post.postId=?1 and obj.user.userId=?2")
	Favorites findOneLikedPost(int postId, String userId);
}
