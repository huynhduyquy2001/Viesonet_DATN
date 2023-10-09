package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ColorsDao;
import com.viesonet.entity.Colors;

@Service
public class ColorsService {
    @Autowired
    ColorsDao colorsDao;

    public List<Colors> getAllColors() {
        return colorsDao.getAllColors();
    }
}
