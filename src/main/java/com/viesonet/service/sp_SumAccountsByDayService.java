package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.SumAccountsByDay;
import com.viesonet.report.sp_SumAccountsByDay;

@Service
public class sp_SumAccountsByDayService {
	@Autowired 
	sp_SumAccountsByDay sp;
	
	public SumAccountsByDay sumAccountByDay() {
		SumAccountsByDay acc = sp.executeSumAccByDays();
		return acc;
	}
}
