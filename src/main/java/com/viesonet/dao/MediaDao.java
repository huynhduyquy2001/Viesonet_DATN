package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viesonet.entity.Media;
import com.viesonet.entity.Message;

public interface MediaDao extends JpaRepository<Media, Integer> {

}
