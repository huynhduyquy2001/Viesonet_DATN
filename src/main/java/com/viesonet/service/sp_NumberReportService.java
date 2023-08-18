package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.NumberReport;
import com.viesonet.report.sp_NumberReport;

@Service
public class sp_NumberReportService {
	@Autowired
	sp_NumberReport sp;
	
	public List<NumberReport> numberReports(int param){
		List<NumberReport> list = sp.executeNumberReport(param);
		return list;
	}
}
