package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.AccountStatusDao;
import com.viesonet.entity.AccountStatus;

@Service
public class AccountStatusService {
	@Autowired
	AccountStatusDao accountStatusDao;
	
	public AccountStatus getById(int statusId) {
		return accountStatusDao.getById(statusId);
	}
}
