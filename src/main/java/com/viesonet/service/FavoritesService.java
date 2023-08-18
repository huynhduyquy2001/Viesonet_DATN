package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.FavoritesDao;
import com.viesonet.dao.PostsDao;
import com.viesonet.entity.Favorites;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

@Service
public class FavoritesService {

	@Autowired
	FavoritesDao favoritesDao;
	
	@Autowired
	PostsDao postsDao;
	
	public List<String> findLikedPosts(String userId) {
		
		return favoritesDao.findLikedPosts(userId);
	}
	public void likepost(Users user, Posts post) {
		Favorites obj = new Favorites();
		obj.setLikeDate(new Date());
		obj.setPost(post);
		obj.setUser(user);
		post.setLikeCount(post.getLikeCount()+1);
		postsDao.saveAndFlush(post);
		favoritesDao.saveAndFlush(obj);
	}
	public void didlikepost(String userId, int postId) {
		Favorites obj = favoritesDao.findOneLikedPost(postId, userId);
		Optional<Posts> optionalPost = postsDao.findById(postId);
		Posts post = optionalPost.orElse(null);
		post.setLikeCount(post.getLikeCount()-1);
		postsDao.saveAndFlush(post);
		favoritesDao.delete(obj);
	}
}
