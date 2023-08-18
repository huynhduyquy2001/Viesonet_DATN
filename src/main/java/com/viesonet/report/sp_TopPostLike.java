package com.viesonet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import com.viesonet.entity.ListAcc;
import com.viesonet.entity.TopPostLike;


public interface sp_TopPostLike extends JpaRepository<TopPostLike, String> {
 
    @Procedure("sp_TopPostLike")
    List<TopPostLike> executeTopLike();
}