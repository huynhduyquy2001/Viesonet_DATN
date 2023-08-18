package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.ListAcc;
import com.viesonet.report.sp_ListAcc;

@Service
public class sp_ListAccService {
	@Autowired
	sp_ListAcc sp;
	
	public List<ListAcc> listAccs(){
		List<ListAcc> list = sp.executeListAcc();
		return list;
	}
}
