package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.viesonet.dao.CommentsDao;
import com.viesonet.dao.PostsDao;
import com.viesonet.entity.Comments;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

@Service
public class CommentsService {

	@Autowired
	CommentsDao commentsDao;
	
	@Autowired
	PostsDao postsDao;
	
	public List<Comments> findCommentsByPostId(int postId){
		return commentsDao.findCommentsByPostId(postId, Sort.by(Sort.Direction.DESC,"commentDate"));
	}
	public Comments addComment(Posts post, Users user, String content) {
		Comments comment = new Comments();
		
		post.setCommentCount(post.getCommentCount()+1);
		postsDao.saveAndFlush(post);
		
		comment.setCommentDate(new Date());
		comment.setContent(content);
		comment.setPost(post);
		comment.setUser(user);
		commentsDao.saveAndFlush(comment);
		return comment;
	}
	
	public Comments getCommentById(int commentId) {
		Optional<Comments> obj = commentsDao.findById(commentId);
		return obj.orElse(null);
	}
}
