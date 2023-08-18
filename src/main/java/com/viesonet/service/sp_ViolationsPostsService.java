package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.ViolationsPosts;
import com.viesonet.report.sp_ViolationsPosts;

@Service
public class sp_ViolationsPostsService {
	@Autowired
	sp_ViolationsPosts sp;
	
	public List<ViolationsPosts> violationsPosts(int param){
		List<ViolationsPosts> list = sp.executeReportViolationPosts(param);
		return list;
	}
}
