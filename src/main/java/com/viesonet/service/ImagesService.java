package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ImagesDao;
import com.viesonet.dao.PostsDao;
import com.viesonet.entity.Images;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

@Service
public class ImagesService {
	
	@Autowired
	ImagesDao imagesDao;
	
	@Autowired
	PostsDao postsDao;
	
	public void saveImage(Posts post, String imageUrl, boolean type) {
		Images image = new Images();
		image.setImageUrl(imageUrl);
		image.setPost(post);
		image.setType(type);
		imagesDao.saveAndFlush(image);
	}
	
	public void saveBackground(Users user, String imageUrl) {
	    // Tạo mới một bài đăng để lưu ảnh bìa
	    Posts post = new Posts();
	    post.setUser(user); // Liên kết bài đăng với người dùng

	    // Lưu bài đăng vào cơ sở dữ liệu để có post ID
	    post = postsDao.save(post);

	    // Tạo một đối tượng hình ảnh và lưu thông tin ảnh bìa
	    Images image = new Images();
	    image.setImageUrl(imageUrl);
	    image.setPost(post);

	    // Lưu hình ảnh vào cơ sở dữ liệu
	    imagesDao.save(image);
	}
	
	public List<Images> getImagesByUserId(String userId) {
        return imagesDao.findImagesByUserId(userId);
    }
	
	public List<Images> getVideosByUserId(String userId) {
        return imagesDao.findVideosByUserId(userId);
    }

}