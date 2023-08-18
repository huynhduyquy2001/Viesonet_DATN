package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.ListYear;
import com.viesonet.report.sp_ListYear;

@Service
public class sp_ListYearService {
	@Autowired
	sp_ListYear sp;
	
	public List<ListYear> listYears(){
		List<ListYear> list = sp.executeListYear();
		return list;
	}
}
