package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.TopPostLike;
import com.viesonet.report.sp_FilterPostLike;

@Service
public class sp_FilterPostLikeService {
	@Autowired
	sp_FilterPostLike sp;
	
	public TopPostLike topPostsLike(int param) {
		TopPostLike top = sp.executeFilterPostLike(param);
		return top;
	}
}
