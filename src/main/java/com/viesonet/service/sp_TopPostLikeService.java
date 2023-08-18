package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.TopPostLike;
import com.viesonet.report.sp_TopPostLike;

@Service
public class sp_TopPostLikeService {
	@Autowired
	sp_TopPostLike sp;
	
	
	public List<TopPostLike> topPostLikes(){
		List<TopPostLike> list = sp.executeTopLike();
		return list;
	}
}
