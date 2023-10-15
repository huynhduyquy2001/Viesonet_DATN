package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.MediaDao;
import com.viesonet.dao.ProductsDao;
import com.viesonet.entity.Media;
import com.viesonet.entity.Products;

@Service
public class MediaService {
    @Autowired
    MediaDao mediaDao;

    @Autowired
    ProductsDao productsDao;

    public Media addMedia(String mediaUrl, Products product, boolean isImage) {
        Media obj = new Media();
        obj.setMediaUrl(mediaUrl);
        obj.setProduct(product);
        obj.setType(isImage);
        mediaDao.save(obj);
        return obj;
    }

    public Media deleteMedia(int media) {
        Media obj = mediaDao.findById(media).orElse(null);
        mediaDao.deleteById(media);
        return obj;
    }
}
