package com.viesonet.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.PostsDao;
import com.viesonet.dao.ReplyDao;
import com.viesonet.entity.Comments;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Reply;
import com.viesonet.entity.Users;

@Service
public class ReplyService {
	@Autowired
	ReplyDao replyDao;
	@Autowired
	PostsDao postsDao;
	public Reply addReply( Users responder, String content, Comments comment, Users receiver, Posts post) {
		Reply obj = new Reply();
		obj.setComment(comment);
		obj.setReceiver(receiver);
		obj.setReplyDate(new Date());
		obj.setReplyContent(content);
		obj.setResponder(responder);
		replyDao.saveAndFlush(obj);
		post.setCommentCount(post.getCommentCount()+1);
		postsDao.saveAndFlush(post);
		return obj;
	}
	public Reply getReplyById(int replyId) {
		Optional<Reply> obj = replyDao.findById(replyId);
		return obj.orElse(null);
	}
}
