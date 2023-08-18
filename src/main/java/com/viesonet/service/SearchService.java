package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Users;

@Service
public class SearchService {
	@Autowired
	UsersDao usersDao;
	
	 public List<Users> searchUsersByUsername(String username) {
	        // Thực hiện tìm kiếm người dùng theo tên người dùng trong DAO
	        return usersDao.findByUsernameContaining(username);
	    }
	 
	 
}
