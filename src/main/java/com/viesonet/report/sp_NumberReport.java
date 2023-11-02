package com.viesonet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.viesonet.entity.NumberReport;
import com.viesonet.entity.ViolationsPosts;

public interface sp_NumberReport extends JpaRepository<NumberReport, Integer> {

    @Procedure("sp_NumberReport")
    List<NumberReport> executeNumberReport(int param1);
}