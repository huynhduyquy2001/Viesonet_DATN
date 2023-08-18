package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.ViolationTypes;

public interface ViolationTypesDao  extends JpaRepository<ViolationTypes, Integer>{
	@Query("SELECT v FROM ViolationTypes v WHERE v.violationTypeId = :violationTypeId")
	ViolationTypes findByViolationTypeId(@Param("violationTypeId") int violationTypeId);
	
	ViolationTypes findByViolationDescription(String violationDescription);
	
	@Query("SELECT v FROM ViolationTypes v WHERE v.violationDescription LIKE %:description%")
	List<Object> findViolationTypes(@Param("description") String description);
}
