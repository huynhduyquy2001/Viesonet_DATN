package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.Last7DaySumAccounts;
import com.viesonet.report.sp_Last7DaySumAccounts;

@Service
public class sp_Last7DaySumAccountsService {
	@Autowired
	sp_Last7DaySumAccounts sp;
	
	public Last7DaySumAccounts find7Day() {
		Last7DaySumAccounts list = sp.executeLast7DaySumAcc();
		return list;
	}
}
