package com.viesonet.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viesonet.entity.Colors;
import com.viesonet.entity.ProductColors;

public interface ProductColorsDao extends JpaRepository<ProductColors, Integer> {

}
