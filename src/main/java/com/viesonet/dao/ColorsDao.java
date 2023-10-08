package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Colors;

public interface ColorsDao extends JpaRepository<Colors, Integer> {
    @Query("SELECT c FROM Colors c ")
    List<Colors> getAllColors();
}
