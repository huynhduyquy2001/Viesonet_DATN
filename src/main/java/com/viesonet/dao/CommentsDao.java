package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Comments;

public interface CommentsDao extends JpaRepository<Comments, Integer> {
	@Query("SELECT obj from Comments obj where obj.post.postId=?1")
	List<Comments> findCommentsByPostId(int postId, Sort sort);
}
