package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.TotalPosts;
import com.viesonet.report.sp_TotalPosts;

@Service
public class sp_TotalPostsService {
	@Autowired
	sp_TotalPosts sp;
	
	public List<TotalPosts> totalPosts(){
		List<TotalPosts> list = sp.executeTotalPosts();
		return list;
	}
}
