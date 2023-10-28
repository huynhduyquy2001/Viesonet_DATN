package com.viesonet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.viesonet.entity.NumberReport;
import com.viesonet.entity.ViolationsPosts;

public interface sp_ViolationsPosts extends JpaRepository<ViolationsPosts, String> {
    @Procedure("sp_ReportViolationPosts")
    List<ViolationsPosts> executeReportViolationPosts(int param1);

}