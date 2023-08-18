package com.viesonet.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.SortOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.viesonet.dao.PostsDao;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

@Service
public class PostsService {

	@Autowired
	PostsDao postsDao;
	
	
	public List<Posts> findPostsByListUserId(List<String> userId) {
		return postsDao.findPostsByListUserId(userId, Sort.by(Sort.Direction.DESC, "postDate"));
	}


	public Posts findPostById(int postId) {
		Optional<Posts> optionalPost = postsDao.findById(postId);
		return optionalPost.orElse(null);
	}

	public Posts post(Users user, String content) {
		// Lấy ngày và giờ hiện tại
		Calendar cal = Calendar.getInstance();
		Date ngayGioDang = cal.getTime();

		// Chuyển đổi sang kiểu Timestamp
		Timestamp timestamp = new Timestamp(ngayGioDang.getTime());
		Posts post = new Posts();
		post.setContent(content);
		post.setCommentCount(0);
		post.setLikeCount(0);
		post.setIsActive(true);
		post.setPostDate(timestamp);
		post.setUser(user);
		return postsDao.saveAndFlush(post);
	}

	public List<Posts> getMyPost(String userId) {
		return postsDao.getMyPosts(userId, Sort.by(Sort.Direction.DESC, "postDate"));
	}

	public int countPost(String userId) {
		return postsDao.countMyPosts(userId);
	}
	

    public List<Posts> findByUserId(String userId) {
        return postsDao.findByUserId(userId);
    }


    public Posts getPostById(int postId) {
        Optional<Posts> postOptional = postsDao.findById(postId);
        return postOptional.orElse(null);
    }

    public void savePost(Posts post) {
    	postsDao.save(post);
    }
    
    public void hidePost(int postId) {
        Posts existingPost = postsDao.findById(postId).orElse(null);
        if (existingPost != null) {
            existingPost.setIsActive(false);
            postsDao.save(existingPost);
        }
    }

	
}
