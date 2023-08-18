package com.viesonet.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Roles;
import com.viesonet.entity.UserInformation;
import com.viesonet.entity.Users;

@Service
public class UsersService {
	@Autowired
	UsersDao usersDao;
	
	@Transactional
    public void updateLoginTime(String userId) {
        Users user = usersDao.getById(userId);
        if (user != null) {
        	// Lấy ngày và giờ hiện tại
            LocalDateTime ngayGioDang = LocalDateTime.now();
            // Chuyển đổi sang kiểu Timestamp
            Timestamp timestamp = Timestamp.valueOf(ngayGioDang);
            user.setAccessTime(timestamp);
            usersDao.saveAndFlush(user);
        }
    }
	

	public Users findUserById(String userId) {
		Optional<Users> user = usersDao.findById(userId);
		return user.orElse(null);
	}

	public boolean existById(String userId) {
		return usersDao.existsById(userId);
	}

	public Users save(Users users) {
		return usersDao.save(users);
	}

	public Users getById(String userId) {
		return usersDao.getById(userId);
	}

	public List<Users> findByUserAndStaff(String userId) {
		return usersDao.findByUserAndStaff(userId);
	}

	public List<Object> findByUserSearch(String userId) {
		return usersDao.findByUserSearch(userId);
	}
	
	public List<Object> findByUserSearchAll(){
		return usersDao.findByUserSearchAll();
	}
	
	public Object findByDetailUser(String userId) {
		return usersDao.findByDetailUser(userId);
	}
	
	public List<Users> findAll(){
		return usersDao.findAll();
	}
	
	public Users setViolationCount(String userId) {
		Users user = usersDao.findByuserId(userId);
		user.setViolationCount(0);
		return usersDao.saveAndFlush(user);
	}
	
	public void updateUserInfo(Users user) {
		Users currentUser = usersDao.findByuserId(user.getUserId());
		currentUser.setUsername(user.getUsername());
        currentUser.setBirthday(user.getBirthday());
        currentUser.setGender(user.isGender());
        currentUser.setAddress(user.getAddress());
        currentUser.setRelationship(user.getRelationship());
        currentUser.setIntroduction(user.getIntroduction());
        currentUser.setAccount(user.getAccount());
		usersDao.saveAndFlush(user);
    }
	 public Users getUserById(String userId) {
	        return usersDao.findById(userId).orElse(null);
	}
	 
	 public void updateBackground(String userId, String newBackgroundImageUrl) {
		    Users user = usersDao.findById(userId).orElse(null);
		    if (user != null) {
		        user.setBackground(newBackgroundImageUrl);
		        usersDao.save(user);
		    }
	}
	 
	 public void updateAvatar(String userId, String newAvatarImageUrl) {
		    Users user = usersDao.findById(userId).orElse(null);
		    if (user != null) {
		        user.setAvatar(newAvatarImageUrl);
		        usersDao.save(user);
		    }
	}
}
