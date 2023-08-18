package com.viesonet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import com.viesonet.entity.ListAcc;


public interface sp_ListAcc extends JpaRepository<ListAcc, String> {
 
    @Procedure("sp_ListAcc")
    List<ListAcc> executeListAcc();
}