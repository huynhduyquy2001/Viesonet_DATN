package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.RolesDao;
import com.viesonet.entity.Roles;

@Service
public class RolesService {
	@Autowired
	RolesDao rolesDao;
	
	public Roles getById(int roleId) {
		return rolesDao.getById(roleId);
	}
}
