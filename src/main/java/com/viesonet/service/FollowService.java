package com.viesonet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.FollowDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Users;

import jakarta.transaction.Transactional;

@Service
public class FollowService {

	@Autowired
	FollowDao followDao;

	public List<Follow> getFollowing(String followingId) {
		return followDao.findByFollowingId(followingId);
	}
	public List<Follow> getFollowers(String followerId){
		return followDao.findByFollowersId(followerId);
	}

	public AccountAndFollow getFollowingFollower(Users user) {
		AccountAndFollow obj = new AccountAndFollow();
		obj.setUser(user);
		obj.setFollowers(followDao.getFollowersById(user));
		obj.setFollowing(followDao.getFollowingById(user));
		return obj;

	}

	public List<Users> getFollowersInfoByUserId(String userId) {
		return followDao.findFollowersInfoByUserId(userId);
	}

	public List<Follow> getAllFollowers() {
		return followDao.findAll();
	}

	public List<Users> getFollowingInfoByUserId(String userId) {
		return followDao.findFollowingInfoByUserId(userId);
	}

	public List<String> findUserIdsOfFollowing(String userId) {
		return followDao.findUserIdsOfFollowing(userId);
	}
	
	public Follow saveFollow(Follow follow) {
        return followDao.save(follow);
    }
	@Transactional
    public void deleteFollowByFollowerAndFollowing(Users follower, Users following) {
        followDao.deleteByFollowerAndFollowing(follower, following);
    }
	public List<Follow> findAllFollow() {
        return followDao.findAll();
    }

}
