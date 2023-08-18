package com.viesonet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ViolationTypesDao;
import com.viesonet.entity.ViolationTypes;

@Service
public class ViolationTypesService {
	@Autowired
	ViolationTypesDao violationTypesDao;
	public List<ViolationTypes> getViolations() {
		return violationTypesDao.findAll();
	}
	public ViolationTypes getById(int id) {
		Optional<ViolationTypes> obj = violationTypesDao.findById(id);
		return obj.orElse(null);
	}
	
	public List<ViolationTypes> findAll() {
		return violationTypesDao.findAll();
	}
	
	public ViolationTypes findByViolationTypesId(int violationTypeId){
		return violationTypesDao.findByViolationTypeId(violationTypeId);
	}
	
	public ViolationTypes findByViolationDescription(String violationDescription) {
        return violationTypesDao.findByViolationDescription(violationDescription);
    }
	
	public ViolationTypes save(ViolationTypes violationTypes) {
		return violationTypesDao.save(violationTypes);
	}
	
	public List<Object> findViolationTypes(String description){
		return violationTypesDao.findViolationTypes(description);
	
	}
}
