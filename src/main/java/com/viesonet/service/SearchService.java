package com.viesonet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;

@Service
public class SearchService {
	@Autowired
	UsersDao usersDao;

	public List<Users> searchUsersByUsername(String username) {
		// Thực hiện tìm kiếm người dùng theo tên người dùng trong DAO
		return usersDao.findByUsernameContaining(username);
	}

	public List<Users> findUserByName(String key) {
		List<Users> list = usersDao.findByUsernameContaining(key);
		if (list.isEmpty()) {
			// Tách chuỗi thành các từ và tìm kiếm theo từng từ
			String[] words = key.split("\\s+");
			Map<String, Users> resultMap = new HashMap<>();

			for (String word : words) {
				List<Users> wordList = usersDao.findByUsernameContaining(word);
				if (!wordList.isEmpty()) {
					// Lặp qua danh sách từng từ để kiểm tra sản phẩm và thêm vào Map nếu chưa có
					for (Users user : wordList) {
						if (!resultMap.containsKey(user.getUserId())) {
							resultMap.put(user.getUserId(), user);
						}
					}
				}
			}

			// Tạo một danh sách mới từ Map resultMap
			List<Users> resultList = new ArrayList<>(resultMap.values());

			return resultList;
		}
		return list;
	}

}
